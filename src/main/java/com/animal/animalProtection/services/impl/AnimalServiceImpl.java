package com.animal.animalProtection.services.impl;


import com.animal.animalProtection.model.Animal;
import com.animal.animalProtection.repository.AnimalRepository;
import com.animal.animalProtection.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalServiceImpl implements AnimalService {
    @Autowired
    private AnimalRepository animalRepository;
    @Override
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }
    @Override
    public void saveAnimal(Animal animal){
        this.animalRepository.save(animal);
    }

    @Override
    public Animal getAnimalById(long id) {
        Optional<Animal> optional = animalRepository.findById(id);
        Animal animal = null;
        if (optional.isPresent()){
            animal= optional.get();
        }else {
            throw new RuntimeException("Animal not found for id ::" +id);
        }
        return animal;
    }

    @Override
    public void deleteAnimalById(long id) {
        this.animalRepository.deleteById(id);
    }

    @Override
    public Page<Animal> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.animalRepository.findAll(pageable);
    }
}
