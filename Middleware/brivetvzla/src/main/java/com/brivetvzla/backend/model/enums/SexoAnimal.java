package com.brivetvzla.backend.model.enums;

/**
 * Sexo del animal.
 * Mapea la columna an_tp_sex en la BD (CHAR(1)).
 *
 * NO_SE se deserializa correctamente desde el JSON de Angular
 * y se trata como null al persistir — la BD guarda null en an_tp_sex.
 */
public enum SexoAnimal {

    /** M — Macho */
    MACHO("M"),

    /** H — Hembra */
    HEMBRA("H"),

    /**
     * null — No se sabe el sexo.
     * Se acepta como valor del enum para que Angular pueda enviarlo
     * explícitamente en vez de omitir el campo.
     * SolicitudService lo trata como null al hacer animal.setSexo().
     */
    NO_SE(null);

    private final String codigo;

    SexoAnimal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static SexoAnimal fromCodigo(String codigo) {
        for (SexoAnimal s : values()) {
            if (s.codigo != null && s.codigo.equalsIgnoreCase(codigo)) return s;
        }
        throw new IllegalArgumentException("Codigo de SexoAnimal invalido: " + codigo);
    }
}
