package com.brivetvzla.backend.service;

import com.brivetvzla.backend.exception.ResourceNotFoundException;
import com.brivetvzla.backend.model.dto.request.ReporteMascotaRequest;
import com.brivetvzla.backend.model.dto.request.VetSolicitudUpdateRequest;
import com.brivetvzla.backend.model.entity.Animal;
import com.brivetvzla.backend.model.entity.Contacto;
import com.brivetvzla.backend.model.entity.Estado;
import com.brivetvzla.backend.model.entity.Solicitud;
import com.brivetvzla.backend.model.entity.Ubicacion;
import com.brivetvzla.backend.model.enums.EspecieAnimal;
import com.brivetvzla.backend.model.enums.EstadoSolicitud;
import com.brivetvzla.backend.model.enums.TipoSolicitud;
import com.brivetvzla.backend.repository.AnimalRepository;
import com.brivetvzla.backend.repository.ContactoRepository;
import com.brivetvzla.backend.repository.EstadoRepository;
import com.brivetvzla.backend.repository.SolicitudRepository;
import com.brivetvzla.backend.repository.UbicacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SolicitudService {

    private final AnimalRepository animalRepository;
    private final ContactoRepository contactoRepository;
    private final UbicacionRepository ubicacionRepository;
    private final EstadoRepository estadoRepository;
    private final SolicitudRepository solicitudRepository;
    private final S3StorageService s3StorageService;

    public SolicitudService(AnimalRepository animalRepository,
                            ContactoRepository contactoRepository,
                            UbicacionRepository ubicacionRepository,
                            EstadoRepository estadoRepository,
                            SolicitudRepository solicitudRepository,
                            S3StorageService s3StorageService) {
        this.animalRepository = animalRepository;
        this.contactoRepository = contactoRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.estadoRepository = estadoRepository;
        this.solicitudRepository = solicitudRepository;
        this.s3StorageService = s3StorageService;
    }

    /**
     * Punto de entrada del controller. Orquesta dos pasos separados:
     *
     * 1. createSolicitudRecord() — transaccional, crea los 4 registros en BD
     *    con placeholders vacíos para las fotos (necesitamos el PK de
     *    solicitud antes de poder nombrar la carpeta de S3).
     *
     * 2. attachFotos() — NO transaccional, sube las fotos a S3 usando el
     *    PK ya generado, y actualiza solo los 2 campos de foto.
     *
     * Se separan a propósito: la subida a S3 es una llamada de red que no
     * debe mantener abierta una transacción de BD, y si S3 falla no
     * queremos revertir los 4 registros que ya son válidos.
     */
    public Solicitud createSolicitud(ReporteMascotaRequest request, List<MultipartFile> fotos) {
        Solicitud solicitud = createSolicitudRecord(request);

        if (fotos != null && !fotos.isEmpty()) {
            solicitud = attachFotos(solicitud, fotos);
        }

        return solicitud;
    }

    /**
     * Crea registros en 4 tablas en orden:
     * 1. contacto
     * 2. estado (lookup) → ubicacion
     * 3. animal
     * 4. solicitud (junta todo, con placeholders de foto vacíos)
     *
     * @Transactional garantiza que si cualquier paso falla,
     * ningún registro queda a medias en la BD.
     */
    @Transactional
    public Solicitud createSolicitudRecord(ReporteMascotaRequest request) {

        // ── 1. Contacto ───────────────────────────────────────────────────────
        Contacto contacto = new Contacto();
        contacto.setNombre(request.getContacto().getNombre());
        contacto.setApellido(request.getContacto().getApellido());
        contacto.setEmail(request.getContacto().getEmail());
        contacto.setTelefono(request.getContacto().getTelefono());
        contacto.setWhatsapp(request.getContacto().getWhatsapp());
        contacto.setMetodoContacto(request.getContacto().getMetodoContacto().getCodigo());
        contacto.setPermitirPublico(request.getContacto().isPermitirDatosPublicos() ? "S" : "N");

        Contacto contactoGuardado = contactoRepository.save(contacto);

        // ── 2. Ubicacion ──────────────────────────────────────────────────────
        Estado estado = estadoRepository.findById(
                request.getUbicacion().getEstadoId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Estado no encontrado: " + request.getUbicacion().getEstadoId())
        );

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setEstado(estado);
        ubicacion.setCiudad(request.getUbicacion().getCiudad());
        ubicacion.setSector(request.getUbicacion().getDireccion());
        ubicacion.setDireccion(request.getUbicacion().getDireccion());
        ubicacion.setReferencia(request.getUbicacion().getReferencia());
        if (request.getUbicacion().getLatitud() != null) {
            ubicacion.setLatitud(BigDecimal.valueOf(request.getUbicacion().getLatitud()));
        }
        if (request.getUbicacion().getLongitud() != null) {
            ubicacion.setLongitud(BigDecimal.valueOf(request.getUbicacion().getLongitud()));
        }

        Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacion);

        // ── 3. Animal ─────────────────────────────────────────────────────────
        Animal animal = new Animal();
        animal.setTipoReporte(request.getTipoSolicitud().getCodigo());
        animal.setNombre(request.getAnimal().getNombre());

        if (request.getAnimal().getEspecie() != null) {
            animal.setEspecie(request.getAnimal().getEspecie().getCodigo());
        }

        animal.setRaza(request.getAnimal().getRaza());
        animal.setColor(request.getAnimal().getColor());

        if (request.getAnimal().getTamanio() != null) {
            animal.setTamanio(request.getAnimal().getTamanio().getCodigo());
        }

        if (request.getAnimal().getSexo() != null) {
            animal.setSexo(request.getAnimal().getSexo().getCodigo());
        }

        Integer edadAproximadaInt = request.getAnimal().getEdadAproximada();
        if (edadAproximadaInt != null) {
            animal.setEdadAproximada(edadAproximadaInt.byteValue());
        }
        animal.setDescripcion(request.getAnimal().getDescripcion());
        animal.setRequiereRevisionVet(request.getAnimal().isRequiereAtencionMedica() ? "S" : "N");

        animal.setUbicacionTexto(request.getUbicacion().getCiudad() + ", " + estado.getNombre());
        animal.setTelefono(request.getContacto().getWhatsapp());

        Animal animalGuardado = animalRepository.save(animal);

        // ── 4. Solicitud ──────────────────────────────────────────────────────
        Solicitud solicitud = new Solicitud();
        solicitud.setAnimal(animalGuardado);
        solicitud.setContacto(contactoGuardado);
        solicitud.setUbicacion(ubicacionGuardada);
        solicitud.setTipo(request.getTipoSolicitud().getCodigo());
        // estado y fechaEvento se asignan automáticamente en @PrePersist ("P" y now())

        // Placeholders — se llenan en attachFotos() una vez tengamos el PK
        solicitud.setS3FolderPath("");
        solicitud.setMainPhotoUrl("");

        return solicitudRepository.save(solicitud);
    }

    /**
     * Sube las fotos a S3 bajo la carpeta de la solicitud (PK zero-padded)
     * y actualiza los campos s3FolderPath / mainPhotoUrl.
     *
     * Si la subida a S3 falla, la solicitud ya creada permanece intacta;
     * el error solo afecta los campos de foto, no los 4 registros base.
     */
    @Transactional
    public Solicitud attachFotos(Solicitud solicitud, List<MultipartFile> fotos) {
        S3StorageService.UploadResult result =
                s3StorageService.uploadFotos(solicitud.getId(), fotos);

        if (!result.folderPath().isEmpty()) {
            solicitud.setS3FolderPath(result.folderPath());
            solicitud.setMainPhotoUrl(result.mainPhotoUrl());
            solicitud = solicitudRepository.save(solicitud);
        }

        return solicitud;
    }

    /**
     * Búsqueda pública de mascotas perdidas/encontradas.
     * Usada por las secciones "Mascotas Perdidas" / "Mascotas Encontradas",
     * y también por "Reportes recientes" del home (sin filtro de tipo, mezcla ambos).
     *
     * @param tipo      "PERDIDA" o "ENCONTRADA", o null para traer ambos tipos mezclados
     * @param especie   "PERRO", "GATO" o null para todas
     * @param estadoId  ID del estado venezolano, o null para todos
     * @param ciudad    texto parcial de ciudad, o null para todas
     */
    public List<Solicitud> searchSolicitudes(TipoSolicitud tipo, EspecieAnimal especie,
                                             Integer estadoId, String ciudad) {
        String tipoCodigo = tipo != null ? tipo.getCodigo() : null;
        String especieCodigo = especie != null ? especie.getCodigo() : null;
        String ciudadFiltro = (ciudad != null && !ciudad.isBlank()) ? ciudad.trim() : null;

        return solicitudRepository.searchSolicitudes(
                tipoCodigo, especieCodigo, estadoId, ciudadFiltro);
    }

    /**
     * Detalle público de una solicitud individual (GET /solicitud/{id}).
     * Excluye rechazadas y eliminadas, igual que la búsqueda.
     *
     * @throws ResourceNotFoundException si no existe o no es visible al público
     */
    public Solicitud getSolicitudPublicaById(Integer id) {
        return solicitudRepository.findByIdPublic(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada: " + id));
    }

    /**
     * Listado de solicitudes para el dashboard veterinario (GET /vet/solicitud).
     * Sin exclusión de estados — el veterinario ve pendientes, rechazadas y
     * eliminadas también, a diferencia de la búsqueda pública.
     *
     * @param estado filtro opcional por estatus; null trae todas
     */
    public List<Solicitud> listSolicitudesForVet(EstadoSolicitud estado) {
        String estadoCodigo = estado != null ? estado.getCodigo() : null;
        return solicitudRepository.findAllForVet(estadoCodigo);
    }

    /**
     * Detalle de una solicitud para el dashboard veterinario (GET /vet/solicitud/{id}).
     * Sin exclusión de estados — a diferencia de getSolicitudPublicaById.
     *
     * @throws ResourceNotFoundException si el id no existe
     */
    public Solicitud getSolicitudForVetById(Integer id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada: " + id));
    }

    /**
     * Actualiza el estatus (y opcionalmente la observación veterinaria) de una
     * solicitud existente. Usado por el dashboard veterinario protegido
     * (PUT /vet/solicitud/{id}).
     *
     * @throws ResourceNotFoundException si el id no existe
     */
    @Transactional
    public Solicitud updateEstadoSolicitud(Integer id, VetSolicitudUpdateRequest request) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada: " + id));

        solicitud.setEstado(request.getEstado().getCodigo());

        if (request.getObservacionVet() != null) {
            solicitud.setObservacionVet(request.getObservacionVet());
        }

        return solicitudRepository.save(solicitud);
    }

    /**
     * Elimina lógicamente una solicitud (soft delete): marca
     * estado = 'E' (Eliminada) en la solicitud, y propaga el mismo
     * flag a animal, contacto y ubicacion asociados — ninguno de
     * estos se comparte entre solicitudes (cada una crea sus propios
     * registros en createSolicitudRecord()), así que es seguro marcarlos
     * sin afectar otros datos.
     *
     * No se hace hard delete a propósito — se conserva todo el historial
     * (incluyendo fotos en S3) para auditoría. La búsqueda pública
     * (Solicitud.search) ya excluye estado 'E' a nivel de solicitud.
     *
     * Usado por el dashboard veterinario protegido
     * (DELETE /vet/solicitud/{id}).
     *
     * @throws ResourceNotFoundException si el id no existe
     */
    @Transactional
    public Solicitud deleteSolicitud(Integer id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada: " + id));

        solicitud.setEstado(EstadoSolicitud.ELIMINADA.getCodigo());
        solicitud = solicitudRepository.save(solicitud);

        Animal animal = solicitud.getAnimal();
        animal.setEstadoAnimal("E");
        animalRepository.save(animal);

        Contacto contacto = solicitud.getContacto();
        contacto.setEstadoContacto("E");
        contactoRepository.save(contacto);

        Ubicacion ubicacion = solicitud.getUbicacion();
        ubicacion.setEstadoRegistro("E");
        ubicacionRepository.save(ubicacion);

        return solicitud;
    }
}
