package com.brivetvzla.backend.repository;

import com.brivetvzla.backend.model.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    List<Estado> findByEstadoOrderByNombreAsc(String estado);
}
