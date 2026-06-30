package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refugio")
public class Refugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_cd_refugio")
    private Integer id;

    // FK → contacto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "re_cd_contacto", nullable = false)
    private Contacto contacto;

    // FK → ubicacion
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "re_ur_cd_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @Column(name = "re_nm_refugio", nullable = false, length = 150)
    private String nombre;

    // 'P'=Pendiente | 'A'=Activo | 'X'=Lleno | 'I'=Inactivo | 'R'=Rechazado
    @Column(name = "re_st_refugio", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "P";

    @Column(name = "re_nu_capacity_total", nullable = false)
    private Short capacidadTotal;

    @Column(name = "re_nu_capacity_available", nullable = false)
    private Short capacidadDisponible;

    // 'G'=Gato | 'P'=Perro | 'A'=Ambos
    @Column(name = "re_tp_species_allowed", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String especieAceptada;

    // 'AH'=Heridos | 'CA'=Cachorros | 'AM'=Adultos mayores
    @Column(name = "re_tp_animal_special_needs", length = 2)
    private String necesidadEspecial;

    // 'S'=Si | 'N'=No
    @Column(name = "re_in_has_pets", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tieneMascotas;

    // 'CP'=Casa-con-patio | 'CS'=Casa-sin-patio | 'AP'=Apartamento
    @Column(name = "re_tp_housing", nullable = false, length = 2)
    private String tipoVivienda;

    // 'C'=Completo | 'P'=Parcial | 'N'=No
    @Column(name = "re_in_fence_housing", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipoCercado;

    @Column(name = "re_de_additional_note", columnDefinition = "TEXT")
    private String notasAdicionales;

    // Solo visible para veterinarios
    @JsonIgnore
    @Column(name = "re_de_observacion_vet", columnDefinition = "TEXT")
    private String observacionVet;

    @Column(name = "re_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "re_dt_updated", nullable = false)
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

    public Contacto getContacto() { return contacto; }
    public void setContacto(Contacto contacto) { this.contacto = contacto; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Short getCapacidadTotal() { return capacidadTotal; }
    public void setCapacidadTotal(Short capacidadTotal) { this.capacidadTotal = capacidadTotal; }

    public Short getCapacidadDisponible() { return capacidadDisponible; }
    public void setCapacidadDisponible(Short capacidadDisponible) { this.capacidadDisponible = capacidadDisponible; }

    public String getEspecieAceptada() { return especieAceptada; }
    public void setEspecieAceptada(String especieAceptada) { this.especieAceptada = especieAceptada; }

    public String getNecesidadEspecial() { return necesidadEspecial; }
    public void setNecesidadEspecial(String necesidadEspecial) { this.necesidadEspecial = necesidadEspecial; }

    public String getTieneMascotas() { return tieneMascotas; }
    public void setTieneMascotas(String tieneMascotas) { this.tieneMascotas = tieneMascotas; }

    public String getTipoVivienda() { return tipoVivienda; }
    public void setTipoVivienda(String tipoVivienda) { this.tipoVivienda = tipoVivienda; }

    public String getTipoCercado() { return tipoCercado; }
    public void setTipoCercado(String tipoCercado) { this.tipoCercado = tipoCercado; }

    public String getNotasAdicionales() { return notasAdicionales; }
    public void setNotasAdicionales(String notasAdicionales) { this.notasAdicionales = notasAdicionales; }

    public String getObservacionVet() { return observacionVet; }
    public void setObservacionVet(String observacionVet) { this.observacionVet = observacionVet; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
