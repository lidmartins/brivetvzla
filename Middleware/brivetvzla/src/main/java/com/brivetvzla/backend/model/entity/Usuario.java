package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "us_cd_user")
    private Integer id;

    // FK → role
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "us_ro_cd_role", nullable = false)
    private Role role;

    @Column(name = "us_nm_first_name", nullable = false, length = 100)
    private String nombre;

    @Column(name = "us_nm_last_name", nullable = false, length = 100)
    private String apellido;

    @Column(name = "us_de_email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "us_de_phone", nullable = false, length = 20)
    private String telefono;

    // NUNCA exponer en JSON bajo ninguna circunstancia
    @JsonIgnore
    @Column(name = "us_de_password_hash", nullable = false, length = 255)
    private String passwordHash;

    // 'S'=Si | 'N'=No
    @Column(name = "us_in_veterinarian", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String esVeterinario = "S";

    // 'A'=Activo | 'I'=Inactivo | 'B'=Bloqueado
    @Column(name = "us_st_user", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado;

    @Column(name = "us_dt_last_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "us_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "us_dt_updated", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Integer getId() { return id; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEsVeterinario() { return esVeterinario; }
    public void setEsVeterinario(String esVeterinario) { this.esVeterinario = esVeterinario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getUltimoLogin() { return ultimoLogin; }
    public void setUltimoLogin(LocalDateTime ultimoLogin) { this.ultimoLogin = ultimoLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
