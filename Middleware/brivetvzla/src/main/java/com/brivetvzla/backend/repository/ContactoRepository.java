package com.brivetvzla.backend.repository;

import com.brivetvzla.backend.model.entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {

    Optional<Contacto> findByEmail(String email);
}
