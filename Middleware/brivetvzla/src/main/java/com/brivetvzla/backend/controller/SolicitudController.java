package com.brivetvzla.backend.controller;

import com.brivetvzla.backend.model.dto.request.ReporteMascotaRequest;
import com.brivetvzla.backend.model.entity.Solicitud;
import com.brivetvzla.backend.model.enums.EspecieAnimal;
import com.brivetvzla.backend.model.enums.TipoSolicitud;
import com.brivetvzla.backend.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<Solicitud> createSolicitud(
            @RequestPart("data") @Valid ReporteMascotaRequest request,
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos) {

        Solicitud createdSolicitud = solicitudService.createSolicitud(request, fotos);
        return ResponseEntity.ok(createdSolicitud);
    }

    /**
     * Búsqueda pública para las secciones "Mascotas Perdidas" / "Mascotas Encontradas".
     *
     * Ejemplos:
     *   GET /solicitud/search?tipo=PERDIDA
     *   GET /solicitud/search?tipo=PERDIDA&especie=PERRO
     *   GET /solicitud/search?tipo=ENCONTRADA&ciudad=Caracas
     *   GET /solicitud/search?tipo=PERDIDA&estadoId=10
     *
     * @param tipo     obligatorio: PERDIDA o ENCONTRADA
     * @param especie  opcional: PERRO o GATO
     * @param estadoId opcional: filtra por estado venezolano
     * @param ciudad   opcional: búsqueda parcial de ciudad
     */
    @GetMapping("/search")
    public ResponseEntity<List<Solicitud>> searchSolicitudes(
            @RequestParam TipoSolicitud tipo,
            @RequestParam(required = false) EspecieAnimal especie,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) String ciudad) {

        List<Solicitud> solicitudes = solicitudService.searchSolicitudes(tipo, especie, estadoId, ciudad);
        return ResponseEntity.ok(solicitudes);
    }
}
