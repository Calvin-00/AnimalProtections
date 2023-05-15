package com.animal.animalProtection.services;



import com.animal.animalProtection.model.Volunteer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VolunteerService {
    List<Volunteer> getAllVolunteers();

    void saveVolunteer(Volunteer volunteer);
    Volunteer getVolunteerById(long id);
    void deleteVolunteerById(long id);
    Page<Volunteer> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
