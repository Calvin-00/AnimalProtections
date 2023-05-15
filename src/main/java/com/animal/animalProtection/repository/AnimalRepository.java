package com.animal.animalProtection.repository;


import com.animal.animalProtection.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
//    @Query("")
//    public List<Animal> findAll(String keyword);
}
