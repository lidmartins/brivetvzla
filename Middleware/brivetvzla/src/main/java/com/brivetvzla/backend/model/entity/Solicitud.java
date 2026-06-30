package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_cd_solicitud")
    private Integer id;

    // FK → animal
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_an_cd_animal", nullable = false)
    private Animal animal;

    // FK → contacto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_co_cd_contacto", nullable = false)
    private Contacto contacto;

    // FK → ubicacion
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_ur_cd_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    // 'P'=Perdida | 'E'=Encontrada
    @Column(name = "so_tp_solicitud", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipo;

    @Column(name = "so_dt_evento", nullable = false)
    private LocalDateTime fechaEvento;

    // 'P'=Pendiente | 'R'=Rechazada | 'A'=Activa
    @Column(name = "so_st_solicitud", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "P";

    // Solo visible para el dashboard veterinario — nunca exponer al público
    @JsonIgnore
    @Column(name = "so_de_observacion_vet", columnDefinition = "TEXT")
    private String observacionVet;

    // Ruta interna S3 — nunca exponer al cliente
    @JsonIgnore
    @Column(name = "so_de_s3_folder_path", nullable = false, length = 500)
    private String s3FolderPath;

    @Column(name = "so_de_main_photo_url", nullable = false, length = 500)
    private String mainPhotoUrl;

    @Column(name = "so_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "so_dt_updated", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (fechaEvento == null) fechaEvento = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Integer getId() { return id; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Contacto getContacto() { return contacto; }
    public void setContacto(Contacto contacto) { this.contacto = contacto; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDateTime fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacionVet() { return observacionVet; }
    public void setObservacionVet(String observacionVet) { this.observacionVet = observacionVet; }

    public String getS3FolderPath() { return s3FolderPath; }
    public void setS3FolderPath(String s3FolderPath) { this.s3FolderPath = s3FolderPath; }

    public String getMainPhotoUrl() { return mainPhotoUrl; }
    public void setMainPhotoUrl(String mainPhotoUrl) { this.mainPhotoUrl = mainPhotoUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
