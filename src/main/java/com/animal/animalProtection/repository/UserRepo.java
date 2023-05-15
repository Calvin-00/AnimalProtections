package com.animal.animalProtection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.animal.animalProtection.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);



}

