package com.brivetvzla.backend.service;

import com.brivetvzla.backend.dto.UbicacionDto;
import com.brivetvzla.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    public UbicacionDto createUbicacion(UbicacionDto ubicacion) {
        return ubicacionRepository.createUbicacion(ubicacion);
    }

    public UbicacionDto updateUbicacion(UbicacionDto ubicacion) {
        return ubicacionRepository.updateUbicacion(ubicacion);
    }

    public void deleteUbicacion(int ubicacionId) {
        ubicacionRepository.deleteUbicacion(ubicacionId);
    }

    public List<UbicacionDto> searchUbicaciones(Integer ubicacionId, Integer estadoId, String city) {
        return ubicacionRepository.searchUbicaciones(ubicacionId, estadoId, city);
    }
}
