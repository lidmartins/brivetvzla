package com.brivetvzla.backend.model.enums;


/**
 * Estatus de una solicitud (reporte de mascota perdida/encontrada).
 * Mapea la columna so_st_solicitud en la BD.
 */
public enum EstadoSolicitud {

    /** P — Recién creada, pendiente de revisión veterinaria. */
    PENDIENTE("P"),

    /** R — Rechazada por el veterinario (ver observacionVet). */
    RECHAZADA("R"),

    /** A — Activa / publicada, visible en las búsquedas públicas. */
    ACTIVA("A"),

    /** C — La mascota fue reunida con su dueño. */
    REUNIDA("C"),

    /** T — La mascota fue adoptada. */
    ADOPTADA("T"),

    /** E — Solicitud eliminada (soft delete) desde el dashboard veterinario. */
    ELIMINADA("E");

    private final String codigo;

    EstadoSolicitud(String codigo) {
        this.codigo = codigo;
    }

    /** Código de un carácter que se persiste en la base de datos. */
    public String getCodigo() {
        return codigo;
    }

    public static EstadoSolicitud fromCodigo(String codigo) {
        for (EstadoSolicitud e : values()) {
            if (e.codigo.equalsIgnoreCase(codigo)) return e;
        }
        throw new IllegalArgumentException("Código de EstadoSolicitud inválido: " + codigo);
    }
}