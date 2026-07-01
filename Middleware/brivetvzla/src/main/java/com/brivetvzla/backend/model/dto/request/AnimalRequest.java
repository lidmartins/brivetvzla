package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.EspecieAnimal;
import com.brivetvzla.backend.model.enums.SexoAnimal;
import com.brivetvzla.backend.model.enums.TamanioAnimal;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Sección "Animal" del formulario "Reportar una mascota".
 * Mapea a la tabla {@code animal} de la BD.
 *
 * <pre>
 * Campos del formulario (paso 2):
 *   - Nombre (opcional)
 *   - Especie: Perro | Gato | Otro
 *   - Raza (opcional)
 *   - Color
 *   - Tamaño: Pequeño | Mediano | Grande
 *   - Sexo: Macho | Hembra | No sé → null
 *   - Edad aproximada (años)
 *   - Descripción / señas particulares (opcional)
 *   - Necesita atención médica urgente (checkbox)
 * </pre>
 */
@Getter
@Setter
public class AnimalRequest {

    /** an_nm_animal — Nombre del animal (puede ser null si es encontrado y desconocido) */
    @Size(max = 100)
    private String nombre;

    /**
     * an_tp_animal — Especie.
     * Null cuando el usuario selecciona "Otro" (no existe código en la BD para "Otro").
     */
    private EspecieAnimal especie;

    /** Texto libre cuando especie == null (el usuario escogió "Otro") */
    @Size(max = 100)
    private String especieOtro;

    /** an_de_breed — Raza (opcional) */
    @Size(max = 100)
    private String raza;

    /** an_de_color — Color principal del pelaje */
    @Size(max = 100)
    private String color;

    /** an_tp_size — Tamaño */
    private TamanioAnimal tamanio;

    /**
     * an_tp_sex — Sexo.
     * Null cuando el usuario selecciona "No sé".
     */
    private SexoAnimal sexo;

    /**
     * an_nu_approx_age — Edad aproximada en años.
     * Null si el usuario no la conoce.
     */
    @Min(0) @Max(30)
    private Integer edadAproximada;

    /** an_de_animal — Descripción / señas particulares */
    @Size(max = 2000)
    private String descripcion;

    /**
     * an_in_require_vet_review — ¿Necesita atención médica urgente?
     * "S" / "N" en la BD; boolean en el DTO.
     */
    private boolean requiereAtencionMedica = false;

}
