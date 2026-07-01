package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body para PUT /vet/solicitud/{id}
 * Lo que envía el dashboard veterinario para actualizar el estatus
 * (y opcionalmente la observación) de una solicitud existente.
 */
@Getter
@Setter
public class VetSolicitudUpdateRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoSolicitud estado;

    // Opcional — motivo de rechazo, nota de seguimiento, etc.
    private String observacionVet;

}
