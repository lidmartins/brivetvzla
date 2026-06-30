package com.brivetvzla.backend.repository;

import com.brivetvzla.backend.model.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    Page<Animal> findByTipoReporte(String tipoReporte, Pageable pageable);

    List<Animal> findByEstadoRevisionOrderByCreatedAtAsc(String estadoRevision);

    List<Animal> findByRefugioId(Integer refugioId);
}
