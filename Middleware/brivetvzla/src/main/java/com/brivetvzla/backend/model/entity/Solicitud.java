package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "solicitud")
@Getter
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_cd_solicitud")
    private Integer id;

    // FK → animal
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_an_cd_animal", nullable = false)
    private Animal animal;

    // FK → contacto
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_co_cd_contacto", nullable = false)
    private Contacto contacto;

    // FK → ubicacion
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "so_ur_cd_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    // 'P'=Perdida | 'E'=Encontrada
    @Setter
    @Column(name = "so_tp_solicitud", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipo;

    @Setter
    @Column(name = "so_dt_evento", nullable = false)
    private LocalDateTime fechaEvento;

    // 'P'=Pendiente | 'R'=Rechazada | 'A'=Activa | 'C'=Reunida | 'T'=Adoptada | 'E'=Eliminada
    @Setter
    @Column(name = "so_st_solicitud", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "P";

    // Siempre visible en el JSON — el backend no filtra este campo, el FE
    // decide si lo muestra o no según la pantalla (público vs dashboard vet).
    @Setter
    @Column(name = "so_de_observacion_vet", columnDefinition = "TEXT")
    private String observacionVet;

    // Ruta interna S3 — nunca exponer al cliente
    @Setter
    @JsonIgnore
    @Column(name = "so_de_s3_folder_path", nullable = false, length = 500)
    private String s3FolderPath;

    @Setter
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
}
