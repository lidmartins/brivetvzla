package com.brivetvzla.backend.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estado")
@Getter
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "es_cd_estado")
    private Integer id;

    @Setter
    @Column(name = "es_cd_country", nullable = false)
    private Integer codigoPais = 58;

    @Setter
    @Column(name = "es_nm_estado", nullable = false, length = 100)
    private String nombre;

    // 'A'=Activo | 'I'=Inactivo
    @Setter
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
}
