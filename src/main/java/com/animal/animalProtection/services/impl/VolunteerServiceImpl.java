package com.animal.animalProtection.services.impl;



import com.animal.animalProtection.model.Volunteer;
import com.animal.animalProtection.repository.VolunteerRepository;
import com.animal.animalProtection.services.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public void saveVolunteer(Volunteer volunteer){
        this.volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer getVolunteerById(long id) {
        Optional<Volunteer> optional = volunteerRepository.findById(id);
        Volunteer volunteer = null;
        if (optional.isPresent()){
            volunteer= optional.get();
        }else {
            throw new RuntimeException("Volunteer not found for id ::" +id);
        }
        return volunteer;
    }

    @Override
    public void deleteVolunteerById(long id) {
        this.volunteerRepository.deleteById(id);
    }

    @Override
    public Page<Volunteer> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.volunteerRepository.findAll(pageable);
    }
}
