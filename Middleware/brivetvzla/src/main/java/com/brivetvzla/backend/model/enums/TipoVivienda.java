package com.brivetvzla.backend.model.enums;

/**
 * Tipo de vivienda donde se ubica el refugio.
 * Mapea re_tp_housing en la BD.
 */
public enum TipoVivienda {

    /** CP — Casa con patio */
    CASA_CON_PATIO("CP"),

    /** CS — Casa sin patio */
    CASA_SIN_PATIO("CS"),

    /** AP — Apartamento */
    APARTAMENTO("AP"),

    /** FN — Finca / terreno (opción visible en el form pero sin código propio en la BD;
     *       usar este valor y mapearlo a un código extendido si se amplía el schema) */
    FINCA("FN");

    private final String codigo;

    TipoVivienda(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
