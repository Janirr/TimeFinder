package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorsRepository extends JpaRepository<Tutor, Long> {
    Tutor findByEmail(String email);
    Tutor findBySubject(String subject);
    Tutor findById(int id);
}
