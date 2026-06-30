package com.brivetvzla.backend.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta — en producción mover a application.properties o variable de entorno
    // Declarada como SecretKey (no Key) para que coincida con .verifyWith() en getClaims()
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // Token válido por 8 horas (turno de trabajo de un veterinario)
    private static final long EXPIRATION_MS = 8 * 60 * 60 * 1000L;

    /**
     * Genera un JWT con el email y rol del veterinario.
     */
    public String generateToken(String email, String roleName) {
        return Jwts.builder()
                .subject(email)
                .claim("role", roleName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            return !getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // API 0.12.x: Jwts.parser() + .verifyWith(SecretKey) — ya sin cast
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}