package com.animal.animalProtection.repository;

import com.animal.animalProtection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepo extends JpaRepository<User, Integer> {
}
