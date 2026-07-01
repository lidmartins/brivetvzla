package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "us_cd_user")
    private Integer id;

    // FK → role
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "us_ro_cd_role", nullable = false)
    private Role role;

    @Setter
    @Column(name = "us_nm_first_name", nullable = false, length = 100)
    private String nombre;

    @Setter
    @Column(name = "us_nm_last_name", nullable = false, length = 100)
    private String apellido;

    @Setter
    @Column(name = "us_de_email", nullable = false, unique = true, length = 100)
    private String email;

    @Setter
    @Column(name = "us_de_phone", nullable = false, length = 20)
    private String telefono;

    // NUNCA exponer en JSON bajo ninguna circunstancia
    @Setter
    @JsonIgnore
    @Column(name = "us_de_password_hash", nullable = false, length = 255)
    private String passwordHash;

    // 'S'=Si | 'N'=No
    @Setter
    @Column(name = "us_in_veterinarian", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String esVeterinario = "S";

    // 'A'=Activo | 'I'=Inactivo | 'B'=Bloqueado
    @Setter
    @Column(name = "us_st_user", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado;

    @Setter
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
}
