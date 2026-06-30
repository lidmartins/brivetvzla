package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "es_cd_estado")
    private Integer id;

    @Column(name = "es_cd_country", nullable = false)
    private Integer codigoPais = 58;

    @Column(name = "es_nm_estado", nullable = false, length = 100)
    private String nombre;

    // 'A'=Activo | 'I'=Inactivo
    @Column(name = "es_st_estado", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "A";

    @Column(name = "es_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "es_dt_updated", nullable = false)
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

    public Integer getCodigoPais() { return codigoPais; }
    public void setCodigoPais(Integer codigoPais) { this.codigoPais = codigoPais; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
