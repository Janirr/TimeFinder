package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.jpa.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByStudentEmail(String email);

    List<Reservation> findAllByTutorId(int tutorId);

    Reservation findById(String id);
}
