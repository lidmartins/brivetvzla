package com.brivetvzla.backend.model.enums;

/**
 * Especie(s) que acepta el refugio.
 * Mapea re_tp_species_allowed en la BD.
 */
public enum EspecieRefugio {

    /** P — Solo perros */
    PERRO("P"),

    /** G — Solo gatos */
    GATO("G"),

    /** A — Ambos */
    AMBOS("A");

    private final String codigo;

    EspecieRefugio(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
