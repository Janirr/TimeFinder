package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {


}
