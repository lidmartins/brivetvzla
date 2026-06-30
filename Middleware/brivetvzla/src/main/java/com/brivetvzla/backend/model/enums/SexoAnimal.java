package com.brivetvzla.backend.model.enums;

/**
 * Sexo del animal.
 * Mapea la columna an_tp_sex en la BD.
 * "No sé" se envía como null desde el frontend.
 */
public enum SexoAnimal {

    /** M — Macho */
    MACHO("M"),

    /** H — Hembra */
    HEMBRA("H");

    private final String codigo;

    SexoAnimal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static SexoAnimal fromCodigo(String codigo) {
        for (SexoAnimal s : values()) {
            if (s.codigo.equalsIgnoreCase(codigo)) return s;
        }
        throw new IllegalArgumentException("Código de SexoAnimal inválido: " + codigo);
    }
}
