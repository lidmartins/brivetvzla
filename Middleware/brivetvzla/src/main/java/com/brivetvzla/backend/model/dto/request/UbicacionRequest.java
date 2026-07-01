package com.brivetvzla.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Sección "Ubicación" — compartida por ReporteMascotaRequest y RegistroRefugioRequest.
 * Mapea a la tabla {@code ubicacion} (+ FK a {@code estado}) de la BD.
 *
 * <pre>
 * Campos del formulario:
 *   - Estado (select con los 24 estados de Venezuela)
 *   - Ciudad / Municipio
 *   - Dirección / sector
 *   - Punto de referencia (opcional)
 *   - Coordenadas GPS (opcionales — para mapa futuro)
 * </pre>
 */
@Getter
@Setter
public class UbicacionRequest {

    /**
     * ur_es_cd_estado — ID del estado venezolano (FK a tabla estado).
     * El frontend envía el ID numérico del estado seleccionado en el select.
     */
    @NotNull(message = "El estado es obligatorio")
    @Positive
    private Integer estadoId;

    /** ur_nm_city — Ciudad o municipio */
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String ciudad;

    /** ur_de_address — Dirección o sector */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255)
    private String direccion;

    /** ur_de_reference — Punto de referencia (opcional) */
    @Size(max = 255)
    private String referencia;

    /**
     * ur_de_latitude / ur_de_longitude — Coordenadas GPS opcionales.
     * Se usan cuando el usuario permite geolocalización en el navegador.
     */
    private Double latitud;
    private Double longitud;

}
