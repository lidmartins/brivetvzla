package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "revision_veterinaria")
public class RevisionVeterinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rv_cd_revision_vet")
    private Integer id;

    // FK → animal
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rv_an_cd_animal", nullable = false)
    private Animal animal;

    // FK → user (el veterinario que revisó)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rv_us_cd_user", nullable = false)
    private Usuario veterinario;

    // 'P'=Pendiente | 'A'=Activo | 'R'=Revisado
    @Column(name = "rv_st_vet_review", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "P";

    @Column(name = "rv_de_comment", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "rv_dt_created", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "rv_dt_updated", nullable = false)
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

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
