package com.brivetvzla.backend.model.enums;

/**
 * Método de contacto preferido.
 * Mapea la columna co_tp_contact_method en la BD.
 */
public enum MetodoContacto {

    /** W — WhatsApp */
    WHATSAPP("W"),

    /** P — Teléfono */
    PHONE("P"),

    /** E — Correo electrónico */
    EMAIL("E"),

    /** A — Cualquiera */
    ANY("A");

    private final String codigo;

    MetodoContacto(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static MetodoContacto fromCodigo(String codigo) {
        for (MetodoContacto m : values()) {
            if (m.codigo.equalsIgnoreCase(codigo)) return m;
        }
        throw new IllegalArgumentException("Código de MetodoContacto inválido: " + codigo);
    }
}
