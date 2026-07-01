package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.TipoCercado;
import com.brivetvzla.backend.model.enums.TipoVivienda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Sección "Condiciones del refugio" (paso 4) del formulario "Registrar Refugio Temporal".
 * Mapea a campos de la tabla {@code refugio} de la BD.
 *
 * <pre>
 * Campos del formulario:
 *   - ¿Tienes mascotas propias en casa? (select, opcional)
 *   - Tipo de vivienda: Casa con patio | Casa sin patio | Apartamento | Finca
 *   - ¿Tienes cercado?: Sí completo | Sí parcial | No
 *   - Notas adicionales (textarea, opcional)
 *   - Checkbox de términos y condiciones
 * </pre>
 */
@Getter
@Setter
public class CondicionesRefugioRequest {

    /**
     * re_in_has_pets — ¿Tiene mascotas propias?
     * null si el usuario no especificó (campo opcional en el form).
     */
    private Boolean tieneMascotasPropias;

    /**
     * Descripción del tipo de mascotas propias (libre), por ejemplo
     * "Sí, perros y gatos". Se almacena en re_de_additional_note
     * o en un campo extendido según evolución del schema.
     */
    @Size(max = 100)
    private String descripcionMascotasPropias;

    /**
     * re_tp_housing — Tipo de vivienda.
     */
    @NotNull(message = "El tipo de vivienda es obligatorio")
    private TipoVivienda tipoVivienda;

    /**
     * re_in_fence_housing — ¿Tiene cercado?
     */
    @NotNull(message = "Indica si tienes cercado")
    private TipoCercado tipoCercado;

    /**
     * re_de_additional_note — Notas adicionales libres (opcional).
     * Alergias, horarios disponibles, condiciones especiales, etc.
     */
    @Size(max = 2000)
    private String notasAdicionales;

    /**
     * Confirmación de términos y condiciones.
     * Debe ser {@code true} para que el request sea válido.
     * No se persiste en la BD; es solo una validación del frontend/backend.
     */
    private boolean aceptaTerminos;

}
