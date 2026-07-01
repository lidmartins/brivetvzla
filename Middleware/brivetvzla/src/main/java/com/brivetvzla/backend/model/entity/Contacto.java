package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contacto")
@Getter
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_cd_contacto")
    private Integer id;

    @Setter
    @Column(name = "co_nm_first_name", nullable = false, length = 100)
    private String nombre;

    @Setter
    @Column(name = "co_nm_last_name", nullable = false, length = 100)
    private String apellido;

    @Setter
    @Column(name = "co_de_email", nullable = false, length = 100)
    private String email;

    @Setter
    @Column(name = "co_de_phone", nullable = false, length = 20)
    private String telefono;

    @Setter
    @Column(name = "co_de_whatsapp", nullable = false, length = 20)
    private String whatsapp;

    // 'W'=WhatsApp | 'P'=Phone | 'E'=Email | 'A'=Any
    @Setter
    @Column(name = "co_tp_contact_method", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String metodoContacto = "W";

    // 'S'=Si | 'N'=No
    @Setter
    @Column(name = "co_in_allow_public", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String permitirPublico = "S";

    // 'A'=Activo | 'E'=Eliminado — se marca cuando se elimina (soft delete) la solicitud asociada
    @Setter
    @Column(name = "co_st_contacto", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estadoContacto = "A";

    @Column(name = "co_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "co_dt_updated", nullable = false)
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
