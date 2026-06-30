package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "an_cd_animal")
    private Integer id;

    // FK → refugio (opcional — el animal puede no estar en ningún refugio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "an_re_cd_refugio")
    private Refugio refugio;

    // 'P'=Perdida | 'E'=Encontrada
    @Column(name = "an_report_type", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipoReporte;

    @Column(name = "an_nm_animal", length = 100)
    private String nombre;

    // 'G'=Gato | 'P'=Perro
    @Column(name = "an_tp_animal", length = 1, columnDefinition = "CHAR(1)")
    private String especie;

    @Column(name = "an_de_breed", length = 100)
    private String raza;

    @Column(name = "an_de_color", length = 100)
    private String color;

    // 'P'=Pequeño | 'M'=Mediano | 'G'=Grande
    @Column(name = "an_tp_size", length = 1, columnDefinition = "CHAR(1)")
    private String tamanio;

    // 'M'=Macho | 'H'=Hembra
    @Column(name = "an_tp_sex", length = 1, columnDefinition = "CHAR(1)")
    private String sexo;

    @Column(name = "an_nu_approx_age")
    private Byte edadAproximada;

    @Column(name = "an_de_animal", columnDefinition = "TEXT")
    private String descripcion;

    // Solo visible para veterinarios — nunca exponer al público
    @JsonIgnore
    @Column(name = "an_in_require_vet_review", length = 1, columnDefinition = "CHAR(1)")
    private String requiereRevisionVet;

    @JsonIgnore
    @Column(name = "an_de_observacion_vet", columnDefinition = "TEXT")
    private String observacionVet;

    // 'P'=Pendiente | 'A'=Activo | 'R'=Revisado
    @Column(name = "an_st_vet_review", length = 1, columnDefinition = "CHAR(1)")
    private String estadoRevision = "P";

    // Campos desnormalizados para búsqueda rápida (ver schema)
    @Column(name = "an_ubicacion", nullable = false, length = 255)
    private String ubicacionTexto;

    @Column(name = "an_telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "an_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "an_dt_updated", nullable = false)
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

    public Refugio getRefugio() { return refugio; }
    public void setRefugio(Refugio refugio) { this.refugio = refugio; }

    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTamanio() { return tamanio; }
    public void setTamanio(String tamanio) { this.tamanio = tamanio; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Byte getEdadAproximada() { return edadAproximada; }
    public void setEdadAproximada(Byte edadAproximada) { this.edadAproximada = edadAproximada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRequiereRevisionVet() { return requiereRevisionVet; }
    public void setRequiereRevisionVet(String requiereRevisionVet) { this.requiereRevisionVet = requiereRevisionVet; }

    public String getObservacionVet() { return observacionVet; }
    public void setObservacionVet(String observacionVet) { this.observacionVet = observacionVet; }

    public String getEstadoRevision() { return estadoRevision; }
    public void setEstadoRevision(String estadoRevision) { this.estadoRevision = estadoRevision; }

    public String getUbicacionTexto() { return ubicacionTexto; }
    public void setUbicacionTexto(String ubicacionTexto) { this.ubicacionTexto = ubicacionTexto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
