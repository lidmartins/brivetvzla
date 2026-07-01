package com.brivetvzla.backend.model.dto.request;

import com.brivetvzla.backend.model.enums.MetodoContacto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
}
