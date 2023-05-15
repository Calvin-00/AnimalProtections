package com.animal.animalProtection.services;

import com.animal.animalProtection.model.User;

import java.util.List;

public interface PeopleService {
    List <User> getAllPeople();
    User getPeopleById(long id);

    void savePeople(User user);
    void deletePeopleById(long id);

}
