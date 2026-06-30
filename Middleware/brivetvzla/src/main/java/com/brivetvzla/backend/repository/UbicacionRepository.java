package com.brivetvzla.backend.repository;

import com.brivetvzla.backend.model.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    List<Ubicacion> findByEstadoId(Integer estadoId);
}
