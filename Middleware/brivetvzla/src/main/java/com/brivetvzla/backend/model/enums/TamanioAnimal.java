package com.brivetvzla.backend.model.enums;

/**
 * Tamaño del animal.
 * Mapea la columna an_tp_size en la BD (CHAR(1)).
 *
 * PEQUENIO es un alias de PEQUENO — ambos mapean al mismo codigo "P".
 * Angular puede enviar cualquiera de los dos y el backend los acepta igual.
 */
public enum TamanioAnimal {

    /** P — Pequeño */
    PEQUENO("P"),

    /** P — Alias con tilde para compatibilidad con el formulario Angular */
    PEQUENIO("P"),

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
        throw new IllegalArgumentException("Codigo de TamanioAnimal invalido: " + codigo);
    }
}