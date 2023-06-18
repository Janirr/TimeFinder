package com.poznan.put.rest.webservice.restapi.jpa;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.poznan.put.rest.webservice.restapi.calendar.AvailableTime;
import com.poznan.put.rest.webservice.restapi.calendar.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.calendar.TimeManager;
import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationJpaResource {
    private final ReservationRepository reservationRepository;
    private final CalendarConfig calendarConfig;

    public ReservationJpaResource(ReservationRepository reservationRepository, CalendarConfig calendarConfig){
        this.reservationRepository = reservationRepository;
        this.calendarConfig = calendarConfig;
    }

    @GetMapping
    public List<Reservation> retrieveAllReservations(){
        return reservationRepository.findAll();
    }

    @GetMapping("/reservation/{reservationId}")
    public Reservation retrieveReservationById(@PathVariable int reservationId){
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFound("There is no reservation with id: "+reservationId)
        );
    }

    @GetMapping("/google/tutor/{tutorId}/calendar/{calendarId}")
    public List<Event> getCalendarEventsFromCalendarByIds(@PathVariable int tutorId, @PathVariable String calendarId)
            throws GeneralSecurityException, IOException {
        return calendarConfig.getEventsFromCalendarById(tutorId, calendarId);
    }

    @GetMapping("/tutor/{tutorId}/calendar/{calendarId}/{minutesForLesson}")
    public ArrayList<ArrayList<AvailableTime>> getFreeTime(@PathVariable int tutorId, @PathVariable String calendarId, @PathVariable int minutesForLesson)
            throws GeneralSecurityException, IOException {
        TimeManager timeManager = new TimeManager();
        return timeManager.getFreeTime(tutorId, calendarId, minutesForLesson);
    }



    @PostMapping
    public ResponseEntity<Reservation> createNewReservation(@Valid @RequestBody Reservation reservation){
        // add Student into the ArrayList in Service
        Reservation savedReservation = reservationRepository.save(reservation);
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

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable int id){
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFound("There is no student with id: "+id);
        }
        reservationRepository.deleteById(id);
    }

    @PostMapping("/tutor/{tutorId}/calendar/{calendarId}")
    public void addReservationToCalendar(@PathVariable int tutorId, @PathVariable String calendarId)
            throws GeneralSecurityException, IOException {
        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2023-05-09T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2023-05-09T10:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));
        calendarConfig.addEventToCalendar(tutorId, event, calendarId);
    }

    @PutMapping("/calendar/{calendarId}/event/{eventId}")
    public void updateEvent(@PathVariable String calendarId, @PathVariable String eventId)
            throws GeneralSecurityException, IOException {
        System.out.printf("Updating event %s\n", eventId);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime("2023-06-24T14:00:00-07:00"))
                .setTimeZone("Poland");
        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime("2023-06-24T16:00:00-07:00"))
                .setTimeZone("Poland");

        calendarConfig.editEventById(1, calendarId, eventId, start, end);
    }

    @GetMapping("/calendar/{calendarId}/event/{eventId}")
    public Event getEvent(@PathVariable String calendarId, @PathVariable String eventId)
            throws GeneralSecurityException, IOException {
        System.out.printf("Getting event %s\n", eventId);
        return calendarConfig.getEventById(1, calendarId, eventId);
    }

}
