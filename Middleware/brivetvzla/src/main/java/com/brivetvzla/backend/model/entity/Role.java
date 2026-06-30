package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ro_cd_role")
    private Integer id;

    // 'ADMIN' | 'VET' | 'PUBLICO' | 'SUPERADMIN'
    @Column(name = "ro_nm_role", nullable = false, unique = true, length = 20)
    private String nombre;

    // 'A'=Activo | 'I'=Inactivo
    @Column(name = "ro_st_role", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "A";

    @Column(name = "ro_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ro_dt_updated", nullable = false)
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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
