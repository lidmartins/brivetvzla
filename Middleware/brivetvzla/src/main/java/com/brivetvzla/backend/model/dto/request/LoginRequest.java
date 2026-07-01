package com.brivetvzla.backend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body para POST /auth/login
 * Lo que envía el formulario de acceso veterinario.
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
