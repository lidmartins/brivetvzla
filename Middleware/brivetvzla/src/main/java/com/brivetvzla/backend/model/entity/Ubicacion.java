package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ur_cd_ubicacion")
    private Integer id;

    // FK → estado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ur_es_cd_estado", nullable = false)
    private Estado estado;

    @Column(name = "ur_nm_city", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "ur_nm_sector", nullable = false, length = 150)
    private String sector;

    @Column(name = "ur_de_address", nullable = false, length = 255)
    private String direccion;

    @Column(name = "ur_de_reference", length = 255)
    private String referencia;

    @Column(name = "ur_de_postal_code", length = 10)
    private String codigoPostal;

    @Column(name = "ur_nu_latitude", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "ur_nu_longitude", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "ur_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ur_dt_updated", nullable = false)
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

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
