package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.EspecieRefugio;
import com.brivetvzla.backend.model.enums.NecesidadEspecial;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


/**
 * Sección "Animales" del formulario "Registrar Refugio Temporal" (paso 3).
 * Mapea a campos de la tabla {@code refugio} de la BD.
 *
 * <pre>
 * Campos del formulario:
 *   - Especie que puedes recibir: Perros | Gatos | Ambos (tarjetas de selección)
 *   - Cantidad que puedes recibir: 1 | 2 | 3–5 | 6–10 | Más de 10
 *   - Tiempo disponible: Días | Una semana | Dos semanas | Un mes | Indefinido
 *   - Necesidades especiales (checkboxes, opcional):
 *       · Animales heridos o en recuperación
 *       · Cachorros o crías sin madre
 *       · Animales adultos mayores
 * </pre>
 */
@Getter
@Setter
public class AnimalesRefugioRequest {

    /**
     * re_tp_species_allowed — Especie(s) que acepta el refugio.
     */
    @NotNull(message = "Debes indicar qué especie puedes recibir")
    private EspecieRefugio especieAceptada;

    /**
     * re_nu_capacity_total — Capacidad total (número máximo de animales).
     * El frontend envía un número; el select del formulario ofrece opciones
     * predefinidas (1, 2, 5, 10, 15) como valores representativos.
     */
    @NotNull(message = "La capacidad es obligatoria")
    @Min(1) @Max(999)
    private Integer capacidadTotal;

    /**
     * Tiempo estimado de disponibilidad en días.
     * El frontend mapea las opciones del select a valores numéricos:
     *   Días (1–7)       → 7
     *   Una semana       → 7
     *   Dos semanas      → 14
     *   Un mes o más     → 30
     *   Indefinido       → null (sin límite)
     */
    private Integer diasDisponible;

    /**
     * re_tp_animal_special_needs — Necesidades especiales que puede atender.
     * Lista vacía si no tiene capacidad para ninguna.
     */
    private List<NecesidadEspecial> necesidadesEspeciales;

}
