package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacto")
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_cd_contacto")
    private Integer id;

    @Column(name = "co_nm_first_name", nullable = false, length = 100)
    private String nombre;

    @Column(name = "co_nm_last_name", nullable = false, length = 100)
    private String apellido;

    @Column(name = "co_de_email", nullable = false, length = 100)
    private String email;

    @Column(name = "co_de_phone", nullable = false, length = 20)
    private String telefono;

    @Column(name = "co_de_whatsapp", nullable = false, length = 20)
    private String whatsapp;

    // 'W'=WhatsApp | 'P'=Phone | 'E'=Email | 'A'=Any
    @Column(name = "co_tp_contact_method", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String metodoContacto = "W";

    // 'S'=Si | 'N'=No
    @Column(name = "co_in_allow_public", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String permitirPublico = "S";

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

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Integer getId() { return id; }

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

    public String getMetodoContacto() { return metodoContacto; }
    public void setMetodoContacto(String metodoContacto) { this.metodoContacto = metodoContacto; }

    public String getPermitirPublico() { return permitirPublico; }
    public void setPermitirPublico(String permitirPublico) { this.permitirPublico = permitirPublico; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
