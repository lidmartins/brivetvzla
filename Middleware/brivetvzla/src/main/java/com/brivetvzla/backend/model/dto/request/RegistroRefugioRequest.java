package com.brivetvzla.backend.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body para el endpoint:
 * {@code POST /api/v1/refugios}
 *
 * <p>Cubre el modal "Registrar Refugio Temporal".
 *
 * <p>El flujo del formulario tiene 4 pasos:
 * <ol>
 *   <li>Contacto    → {@code contacto}   — ¿Quién ofrece el refugio?</li>
 *   <li>Ubicación   → {@code ubicacion}  — ¿Dónde está el refugio?</li>
 *   <li>Animales    → {@code animales}   — ¿Qué animales puedes recibir?</li>
 *   <li>Condiciones → {@code condiciones} — Condiciones del refugio</li>
 * </ol>
 *
 * <p>Este request es un JSON plano (sin archivos adjuntos);
 * los refugios no requieren foto en el formulario actual.
 */
public class RegistroRefugioRequest {

    /**
     * re_nm_refugio — Nombre del refugio o alias público.
     * Si el voluntario no lo provee, el backend puede generarlo
     * automáticamente como "Refugio de {nombre}".
     */
    @Size(max = 150)
    private String nombreRefugio;

    /**
     * Paso 1 — Datos del voluntario que ofrece el espacio.
     * Genera o reutiliza un registro en la tabla {@code contacto}.
     */
    @NotNull(message = "Los datos de contacto son obligatorios")
    @Valid
    private ContactoRequest contacto;

    /**
     * Paso 2 — Ubicación del refugio.
     * Genera un registro en la tabla {@code ubicacion}.
     */
    @NotNull(message = "La ubicación es obligatoria")
    @Valid
    private UbicacionRequest ubicacion;

    /**
     * Paso 3 — Capacidad y tipo de animales.
     * Mapea a campos de la tabla {@code refugio}.
     */
    @NotNull(message = "Los datos de animales son obligatorios")
    @Valid
    private AnimalesRefugioRequest animales;

    /**
     * Paso 4 — Condiciones físicas del espacio.
     * Mapea a campos de la tabla {@code refugio}.
     */
    @NotNull(message = "Las condiciones del refugio son obligatorias")
    @Valid
    private CondicionesRefugioRequest condiciones;

    /**
     * Validación de términos: el checkbox de confirmación del paso 4
     * debe estar marcado para que el backend procese el registro.
     */
    @AssertTrue(message = "Debes aceptar los términos para registrar el refugio")
    public boolean isTerminosAceptados() {
        return condiciones != null && condiciones.isAceptaTerminos();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getNombreRefugio() { return nombreRefugio; }
    public void setNombreRefugio(String nombreRefugio) { this.nombreRefugio = nombreRefugio; }

    public ContactoRequest getContacto() { return contacto; }
    public void setContacto(ContactoRequest contacto) { this.contacto = contacto; }

    public UbicacionRequest getUbicacion() { return ubicacion; }
    public void setUbicacion(UbicacionRequest ubicacion) { this.ubicacion = ubicacion; }

    public AnimalesRefugioRequest getAnimales() { return animales; }
    public void setAnimales(AnimalesRefugioRequest animales) { this.animales = animales; }

    public CondicionesRefugioRequest getCondiciones() { return condiciones; }
    public void setCondiciones(CondicionesRefugioRequest condiciones) { this.condiciones = condiciones; }
}
