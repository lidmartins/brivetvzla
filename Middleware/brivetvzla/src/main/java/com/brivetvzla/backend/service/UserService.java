package com.brivetvzla.backend.service;

import com.brivetvzla.backend.exception.UnauthorizedException;
import com.brivetvzla.backend.filter.JwtUtil;
import com.brivetvzla.backend.model.dto.request.LoginRequest;
import com.brivetvzla.backend.model.entity.Usuario;
import com.brivetvzla.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Valida credenciales y devuelve la entity Usuario + token JWT.
     * El passwordHash nunca sale en el JSON gracias a @JsonIgnore en la entity.
     */
    public LoginResponse loginUser(LoginRequest request) {

        // 1. Buscar usuario por email
        Usuario usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("usuario/contraseña incorrectos"));

        // 2. Verificar que esté activo
        if (!"A".equals(usuario.getEstado())) {
            throw new UnauthorizedException("usuario inactivo o bloqueado");
        }

        // 3. Comparar contraseña con BCrypt
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new UnauthorizedException("usuario/contraseña incorrectos");
        }

        // 4. Actualizar último login
        usuario.setUltimoLogin(LocalDateTime.now());
        userRepository.save(usuario);

        // 5. Generar JWT con email y nombre del rol
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getRole().getNombre()
        );

        return new LoginResponse(usuario, token);
    }

    // ── Inner class: lo que devuelve el endpoint de login ─────────────────────
    // No es un DTO separado — vive aquí porque solo se usa en este Service.
    public static class LoginResponse {
        private final Usuario usuario;
        private final String token;

        public LoginResponse(Usuario usuario, String token) {
            this.usuario = usuario;
            this.token = token;
        }

        public Usuario getUsuario() { return usuario; }
        public String getToken() { return token; }
    }
}
