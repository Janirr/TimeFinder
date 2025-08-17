package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorsRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByEmail(String email);

    Optional<Tutor> findByEmailAndPassword(String email, String password);

    Optional<Tutor> findBySubject(String subject);

    Optional<Tutor> findById(int id);

    Optional<Tutor> findByPhoneNumber(String phoneNumber);
}
