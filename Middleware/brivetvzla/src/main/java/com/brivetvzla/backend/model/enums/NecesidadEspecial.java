package com.brivetvzla.backend.model.enums;

/**
 * Tipos de necesidades especiales que el refugio puede atender.
 * Mapea re_tp_animal_special_needs en la BD.
 * El campo es multivalor (lista), se serializa en el backend
 * como cadena separada por comas o como columna SET según implementación.
 */
public enum NecesidadEspecial {

    /** AH — Animales heridos o en recuperación */
    HERIDOS_RECUPERACION("AH"),

    /** CA — Cachorros o crías sin madre */
    CACHORROS("CA"),

    /** AM — Animales adultos mayores */
    ADULTOS_MAYORES("AM");

    private final String codigo;

    NecesidadEspecial(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
