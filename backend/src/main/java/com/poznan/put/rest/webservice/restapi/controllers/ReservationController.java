package com.poznan.put.rest.webservice.restapi.controllers;

import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.controllers.responses.AvailableTimeResponse;
import com.poznan.put.rest.webservice.restapi.jpa.model.Reservation;
import com.poznan.put.rest.webservice.restapi.services.ReservationService;
import com.poznan.put.rest.webservice.restapi.services.helpers.ShortEvent;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/{reservationId}")
    public Reservation retrieveReservationById(@PathVariable int reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @GetMapping("/google/tutor/{tutorId}/calendar/{calendarId}")
    public List<Event> getCalendarEventsFromCalendarByIds(@PathVariable int tutorId)
            throws GeneralSecurityException, IOException {
        return reservationService.getCalendarEventsFromCalendar(tutorId);
    }

    @GetMapping("/tutor/{tutorId}/{minutesForLesson}")
    public HashMap<LocalDate, List<AvailableTimeResponse>> getFreeTime(@PathVariable int tutorId, @PathVariable int minutesForLesson) {
        return reservationService.getFreeTime(tutorId, minutesForLesson);
    }

    @GetMapping
    public List<Reservation> retrieveAllReservationsForStudent(@RequestParam String email) {
        return reservationService.getAllReservationsForStudent(email);
    }

    @PostMapping
    public ResponseEntity<Reservation> createNewReservation(@Valid @RequestBody Reservation reservation) {
        Reservation savedReservation = reservationService.createReservation(reservation);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public void deleteReservationById(@PathVariable int id) {
        reservationService.deleteReservationById(id);
    }

    @PostMapping("/tutor/{tutorId}")
    public void addReservationToCalendar(@PathVariable int tutorId, @RequestBody ShortEvent shortEvent)
            throws GeneralSecurityException, IOException {
        reservationService.addReservationToCalendar(tutorId, shortEvent);
    }

    @GetMapping("/tutor/{tutorId}/event/{eventId}")
    public Event getEvent(@PathVariable int tutorId, @PathVariable String eventId)
            throws GeneralSecurityException, IOException {
        return reservationService.getEventById(tutorId, eventId);
    }

    @GetMapping("/tutor/{tutorId}")
    public void synchronizeGoogleCalendarWithReservations(@PathVariable int tutorId)
            throws GeneralSecurityException, IOException {
        reservationService.synchronizeGoogleCalendarWithReservations(tutorId);
    }
}
