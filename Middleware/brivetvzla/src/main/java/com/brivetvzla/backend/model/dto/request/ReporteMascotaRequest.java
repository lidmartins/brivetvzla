package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.TipoSolicitud;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body para el endpoint:
 * {@code POST /api/v1/solicitudes}
 *
 * <p>Cubre el modal "Reportar una mascota" en sus dos modalidades:
 * <ul>
 *   <li><b>PERDIDA</b>  — el usuario perdió a su animal</li>
 *   <li><b>ENCONTRADA</b> — el usuario rescató/encontró un animal ajeno</li>
 * </ul>
 *
 * <p>El flujo del formulario tiene 5 pasos:
 * <ol>
 *   <li>Tipo        → {@code tipoSolicitud}</li>
 *   <li>Animal      → {@code animal}</li>
 *   <li>Ubicación   → {@code ubicacion}</li>
 *   <li>Contacto    → {@code contacto}</li>
 *   <li>Foto        → las fotos se suben como {@code multipart/form-data}
 *                     y el backend las almacena en S3; las URLs se guardan
 *                     en {@code so_de_main_photo_url} y {@code so_de_s3_folder_path}</li>
 * </ol>
 *
 * <p><b>Nota sobre fotos:</b> este objeto se envía como parte de un
 * {@code multipart/form-data}. El campo JSON del formulario se llama {@code data}
 * y las imágenes en {@code fotos[]}. El controlador usa
 * {@code @RequestPart("data") ReporteMascotaRequest} y
 * {@code @RequestPart("fotos") List<MultipartFile>}.
 */
@Getter
@Setter
public class ReporteMascotaRequest {

    /**
     * Paso 1 — Tipo de reporte.
     * so_tp_solicitud / an_report_type en la BD.
     */
    @NotNull(message = "El tipo de solicitud es obligatorio")
    private TipoSolicitud tipoSolicitud;

    /**
     * Paso 2 — Información del animal.
     * Genera registros en la tabla {@code animal}.
     */
    @NotNull(message = "Los datos del animal son obligatorios")
    @Valid
    private AnimalRequest animal;

    /**
     * Paso 3 — Ubicación del evento (donde se perdió / encontró).
     * Genera registros en la tabla {@code ubicacion}.
     */
    @NotNull(message = "La ubicación es obligatoria")
    @Valid
    private UbicacionRequest ubicacion;

    /**
     * Paso 4 — Datos del reportante.
     * Genera registros en la tabla {@code contacto}.
     */
    @NotNull(message = "Los datos de contacto son obligatorios")
    @Valid
    private ContactoRequest contacto;

    /*
     * Paso 5 — Fotos
     * Las fotos NO se incluyen en este objeto JSON.
     * Se reciben por separado como List<MultipartFile> fotos
     * en el mismo multipart/form-data request.
     * El controlador se encarga de subirlas a S3 y almacenar las URLs.
     */

}
