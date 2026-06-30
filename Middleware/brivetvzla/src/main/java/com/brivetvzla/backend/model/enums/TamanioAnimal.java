package com.brivetvzla.backend.model.enums;
/**
 * Tamaño del animal.
 * Mapea la columna an_tp_size en la BD.
 */
public enum TamanioAnimal {

    /** P — Pequeño */
    PEQUENO("P"),

    /** M — Mediano */
    MEDIANO("M"),

    /** G — Grande */
    GRANDE("G");

    private final String codigo;

    TamanioAnimal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TamanioAnimal fromCodigo(String codigo) {
        for (TamanioAnimal t : values()) {
            if (t.codigo.equalsIgnoreCase(codigo)) return t;
        }
        throw new IllegalArgumentException("Código de TamanioAnimal inválido: " + codigo);
    }
}
