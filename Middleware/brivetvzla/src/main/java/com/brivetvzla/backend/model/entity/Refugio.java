package com.brivetvzla.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refugio")
@Getter
public class Refugio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_cd_refugio")
    private Integer id;

    // FK → contacto
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "re_cd_contacto", nullable = false)
    private Contacto contacto;

    // FK → ubicacion
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "re_ur_cd_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @Setter
    @Column(name = "re_nm_refugio", nullable = false, length = 150)
    private String nombre;

    // 'P'=Pendiente | 'A'=Activo | 'X'=Lleno | 'I'=Inactivo | 'R'=Rechazado
    @Setter
    @Column(name = "re_st_refugio", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estado = "P";

    @Setter
    @Column(name = "re_nu_capacity_total", nullable = false)
    private Short capacidadTotal;

    @Setter
    @Column(name = "re_nu_capacity_available", nullable = false)
    private Short capacidadDisponible;

    // 'G'=Gato | 'P'=Perro | 'A'=Ambos
    @Setter
    @Column(name = "re_tp_species_allowed", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String especieAceptada;

    // 'AH'=Heridos | 'CA'=Cachorros | 'AM'=Adultos mayores
    @Setter
    @Column(name = "re_tp_animal_special_needs", length = 2)
    private String necesidadEspecial;

    // 'S'=Si | 'N'=No
    @Setter
    @Column(name = "re_in_has_pets", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tieneMascotas;

    // 'CP'=Casa-con-patio | 'CS'=Casa-sin-patio | 'AP'=Apartamento
    @Setter
    @Column(name = "re_tp_housing", nullable = false, length = 2)
    private String tipoVivienda;

    // 'C'=Completo | 'P'=Parcial | 'N'=No
    @Setter
    @Column(name = "re_in_fence_housing", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String tipoCercado;

    @Setter
    @Column(name = "re_de_additional_note", columnDefinition = "TEXT")
    private String notasAdicionales;

    // Solo visible para veterinarios
    @Setter
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
}
