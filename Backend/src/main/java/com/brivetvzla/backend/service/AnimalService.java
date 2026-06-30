package com.brivetvzla.backend.service;

import com.brivetvzla.backend.dto.AnimalDto;
import com.brivetvzla.backend.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public AnimalDto createAnimal(AnimalDto animal) {
        return animalRepository.createAnimal(animal);
    }

    public AnimalDto updateAnimal(AnimalDto animal) {
        return animalRepository.updateAnimal(animal);
    }

    public void deleteAnimal(int animalId) {
        animalRepository.deleteAnimal(animalId);
    }

    public List<AnimalDto> searchAnimals(Integer animalId, String type, String size, String sex, String vetReviewStatus) {
        return animalRepository.searchAnimals(animalId, type, size, sex, vetReviewStatus);
    }
}
