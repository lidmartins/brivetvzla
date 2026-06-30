package com.brivetvzla.backend.model.enums;

/**
 * Tipo de cercado de la vivienda.
 * Mapea re_in_fence_housing en la BD.
 */
public enum TipoCercado {

    /** C — Cercado completo */
    COMPLETO("C"),

    /** P — Cercado parcial */
    PARCIAL("P"),

    /** N — Sin cercado */
    NINGUNO("N");

    private final String codigo;

    TipoCercado(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
