package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.MetodoContacto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Sección "Contacto" — compartida por ReporteMascotaRequest y RegistroRefugioRequest.
 * Mapea directamente a la tabla {@code contacto} de la BD.
 *
 * <pre>
 * Campos del formulario:
 *   - Nombre / Apellido
 *   - Teléfono / WhatsApp
 *   - Correo electrónico
 *   - Método de contacto preferido
 *   - Permitir datos públicos (checkbox)
 * </pre>
 */
public class ContactoRequest {

    /** co_nm_first_name */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    /** co_nm_last_name */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    /** co_de_email */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    @Size(max = 100)
    private String email;

    /** co_de_phone */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20)
    private String telefono;

    /** co_de_whatsapp */
    @NotBlank(message = "El WhatsApp es obligatorio")
    @Size(max = 20)
    private String whatsapp;

    /**
     * co_tp_contact_method
     * Método preferido: WHATSAPP | PHONE | EMAIL | ANY
     */
    @NotNull(message = "El método de contacto es obligatorio")
    private MetodoContacto metodoContacto;

    /**
     * co_in_allow_public
     * ¿Permite que sus datos sean visibles para dueños verificados?
     */
    private boolean permitirDatosPublicos = true;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public MetodoContacto getMetodoContacto() { return metodoContacto; }
    public void setMetodoContacto(MetodoContacto metodoContacto) { this.metodoContacto = metodoContacto; }

    public boolean isPermitirDatosPublicos() { return permitirDatosPublicos; }
    public void setPermitirDatosPublicos(boolean permitirDatosPublicos) { this.permitirDatosPublicos = permitirDatosPublicos; }
}
