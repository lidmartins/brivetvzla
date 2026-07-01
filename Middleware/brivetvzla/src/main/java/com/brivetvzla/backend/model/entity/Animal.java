package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "animal")
@Getter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "an_cd_animal")
    private Integer id;

    // FK → refugio (opcional — el animal puede no estar en ningún refugio)
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "an_re_cd_refugio")
    private Refugio refugio;

    // 'P'=Perdida | 'E'=Encontrada
    @Setter
    @Column(name = "an_tp_report", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipoReporte;

    @Setter
    @Column(name = "an_nm_animal", length = 100)
    private String nombre;

    // 'G'=Gato | 'P'=Perro
    @Setter
    @Column(name = "an_tp_animal", length = 1, columnDefinition = "CHAR(1)")
    private String especie;

    @Setter
    @Column(name = "an_de_breed", length = 100)
    private String raza;

    @Setter
    @Column(name = "an_de_color", length = 100)
    private String color;

    // 'P'=Pequeño | 'M'=Mediano | 'G'=Grande
    @Setter
    @Column(name = "an_tp_size", length = 1, columnDefinition = "CHAR(1)")
    private String tamanio;

    // 'M'=Macho | 'H'=Hembra
    @Setter
    @Column(name = "an_tp_sex", length = 1, columnDefinition = "CHAR(1)")
    private String sexo;

    @Setter
    @Column(name = "an_nu_approx_age")
    private Byte edadAproximada;

    @Setter
    @Column(name = "an_de_animal", columnDefinition = "TEXT")
    private String descripcion;

    // Solo visible para veterinarios — nunca exponer al público
    @Setter
    @JsonIgnore
    @Column(name = "an_in_require_vet_review", length = 1, columnDefinition = "CHAR(1)")
    private String requiereRevisionVet;

    @Setter
    @JsonIgnore
    @Column(name = "an_de_observacion_vet", columnDefinition = "TEXT")
    private String observacionVet;

    // 'P'=Pendiente | 'A'=Activo | 'R'=Revisado
    @Setter
    @Column(name = "an_st_vet_review", length = 1, columnDefinition = "CHAR(1)")
    private String estadoRevision = "P";

    // 'A'=Activo | 'E'=Eliminado — se marca cuando se elimina (soft delete) la solicitud asociada
    @Setter
    @Column(name = "an_st_animal", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estadoAnimal = "A";

    // Campos desnormalizados para búsqueda rápida (ver schema)
    @Setter
    @Column(name = "an_ubicacion", nullable = false, length = 255)
    private String ubicacionTexto;

    @Setter
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
}
