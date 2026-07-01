package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ubicacion")
@Getter
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ur_cd_ubicacion")
    private Integer id;

    // FK → estado
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ur_es_cd_estado", nullable = false)
    private Estado estado;

    @Setter
    @Column(name = "ur_nm_city", nullable = false, length = 100)
    private String ciudad;

    @Setter
    @Column(name = "ur_nm_sector", nullable = false, length = 150)
    private String sector;

    @Setter
    @Column(name = "ur_de_address", nullable = false, length = 255)
    private String direccion;

    @Setter
    @Column(name = "ur_de_reference", length = 255)
    private String referencia;

    @Setter
    @Column(name = "ur_de_postal_code", length = 10)
    private String codigoPostal;

    @Setter
    @Column(name = "ur_nu_latitude", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Setter
    @Column(name = "ur_nu_longitude", precision = 10, scale = 7)
    private BigDecimal longitud;

    // 'A'=Activo | 'E'=Eliminado — se marca cuando se elimina (soft delete) la solicitud asociada.
    // Nombre distinto de "estado" a propósito: ese campo ya es la FK al estado venezolano.
    @Setter
    @Column(name = "ur_st_ubicacion", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String estadoRegistro = "A";

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
}
