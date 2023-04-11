package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.student.StudentNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public class TutorJpaResource {
    private final TutorsRepository TutorsRepository;

    public TutorJpaResource(TutorsRepository tutorsRepository) {
        this.TutorsRepository = tutorsRepository;
    }

    @GetMapping("/tutors")
    public List<Tutor> retrieveAllTutors(){
        return TutorsRepository.findAll();
    }

    @GetMapping("/tutors/{id}")
    public Optional<Tutor> retrieveTutorById(@PathVariable Long id){
        Optional<Tutor> Tutor = TutorsRepository.findById(id);
        if(Tutor.isEmpty()){
            throw new StudentNotFoundException("There is no tutor with id: "+id);
        }
        return Tutor;
    }
}
