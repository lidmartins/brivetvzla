package com.brivetvzla.backend.model.enums;

/**
 * Tipo de reporte de mascota.
 * Mapea la columna so_tp_solicitud / an_report_type en la BD.
 */
public enum TipoSolicitud {

    /** P — El usuario perdió a su animal. */
    PERDIDA("P"),

    /** E — El usuario encontró / rescató un animal ajeno. */
    ENCONTRADA("E");

    private final String codigo;

    TipoSolicitud(String codigo) {
        this.codigo = codigo;
    }

    /** Código de un carácter que se persiste en la base de datos. */
    public String getCodigo() {
        return codigo;
    }

    public static TipoSolicitud fromCodigo(String codigo) {
        for (TipoSolicitud t : values()) {
            if (t.codigo.equalsIgnoreCase(codigo)) return t;
        }
        throw new IllegalArgumentException("Código de TipoSolicitud inválido: " + codigo);
    }
}
