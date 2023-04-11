package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationJpaResource {
    private final ReservationRepository ReservationRepository;
    public ReservationJpaResource(ReservationRepository reservationRepository) {
        this.ReservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    public List<Reservation> retrieveAllReservations(){
        return ReservationRepository.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createNewReservation(@Valid @RequestBody Reservation reservation){
        // add Student into the ArrayList in Service
        Reservation savedReservation = ReservationRepository.save(reservation);
        // get new Location for the Student to be created in
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        /* return the ResponseEntity when trying to
           create Student in location and build it   */
        return ResponseEntity.created(location).build();
    }


}
