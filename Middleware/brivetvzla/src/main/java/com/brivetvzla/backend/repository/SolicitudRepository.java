package com.brivetvzla.backend.repository;

import com.brivetvzla.backend.model.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    /**
     * Búsqueda pública de mascotas perdidas/encontradas.
     * El JPQL real vive centralizado en META-INF/orm.xml bajo el nombre "Solicitud.search".
     * Edita ahí, no aquí, cuando necesites ajustar el query.
     */
    @Query(name = "Solicitud.search")
    List<Solicitud> searchSolicitudes(
            @Param("tipo") String tipo,
            @Param("especie") String especie,
            @Param("estadoId") Integer estadoId,
            @Param("ciudad") String ciudad
    );
}