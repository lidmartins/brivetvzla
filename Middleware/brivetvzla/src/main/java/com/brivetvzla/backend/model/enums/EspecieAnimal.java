package com.brivetvzla.backend.model.enums;

/**
 * Especie del animal.
 * Mapea la columna an_tp_animal en la BD.
 * El formulario también muestra "Otro", que se persiste como null.
 */
public enum EspecieAnimal {

    /** P — Perro */
    PERRO("P"),

    /** G — Gato */
    GATO("G");

    private final String codigo;

    EspecieAnimal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EspecieAnimal fromCodigo(String codigo) {
        for (EspecieAnimal e : values()) {
            if (e.codigo.equalsIgnoreCase(codigo)) return e;
        }
        throw new IllegalArgumentException("Código de EspecieAnimal inválido: " + codigo);
    }
}
