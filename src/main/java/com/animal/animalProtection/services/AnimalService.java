package com.animal.animalProtection.services;


import com.animal.animalProtection.model.Animal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnimalService {
    List<Animal> getAllAnimals();

    void saveAnimal(Animal animal);
    Animal getAnimalById(long id);
    void deleteAnimalById(long id);
    Page<Animal> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
