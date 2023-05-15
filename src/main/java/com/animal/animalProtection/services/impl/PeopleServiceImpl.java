package com.animal.animalProtection.services.impl;

import com.animal.animalProtection.model.User;
import com.animal.animalProtection.repository.PeopleRepo;
import com.animal.animalProtection.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private PeopleRepo peopleRepo;
    @Override
    public List<User> getAllPeople() {
        return peopleRepo.findAll();
    }

    @Override
    public User getPeopleById(long id) {
        Optional< User > optional = peopleRepo.findById((int) id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return user;
    }

    @Override
    public void savePeople(User user) {
        this.peopleRepo.save(user);
    }

    @Override
    public void deletePeopleById(long id) {
        this.peopleRepo.deleteById((int) id);
    }
}
