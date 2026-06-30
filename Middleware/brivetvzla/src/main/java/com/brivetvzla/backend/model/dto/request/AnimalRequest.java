package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.EspecieAnimal;
import com.brivetvzla.backend.model.enums.SexoAnimal;
import com.brivetvzla.backend.model.enums.TamanioAnimal;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

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

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public EspecieAnimal getEspecie() { return especie; }
    public void setEspecie(EspecieAnimal especie) { this.especie = especie; }

    public String getEspecieOtro() { return especieOtro; }
    public void setEspecieOtro(String especieOtro) { this.especieOtro = especieOtro; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public TamanioAnimal getTamanio() { return tamanio; }
    public void setTamanio(TamanioAnimal tamanio) { this.tamanio = tamanio; }

    public SexoAnimal getSexo() { return sexo; }
    public void setSexo(SexoAnimal sexo) { this.sexo = sexo; }

    public Integer getEdadAproximada() { return edadAproximada; }
    public void setEdadAproximada(Integer edadAproximada) { this.edadAproximada = edadAproximada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isRequiereAtencionMedica() { return requiereAtencionMedica; }
    public void setRequiereAtencionMedica(boolean requiereAtencionMedica) { this.requiereAtencionMedica = requiereAtencionMedica; }
}
