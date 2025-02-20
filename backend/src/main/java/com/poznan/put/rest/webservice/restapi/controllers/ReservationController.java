package com.poznan.put.rest.webservice.restapi.controllers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.ReservationRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.model.Reservation;
import com.poznan.put.rest.webservice.restapi.model.ShortEvent;
import com.poznan.put.rest.webservice.restapi.model.Student;
import com.poznan.put.rest.webservice.restapi.model.Tutor;
import com.poznan.put.rest.webservice.restapi.model.records.AvailableTime;
import com.poznan.put.rest.webservice.restapi.services.StudentService;
import com.poznan.put.rest.webservice.restapi.services.TimeManagerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final CalendarConfig calendarConfig;
    private final StudentService studentService;
    private final TutorsRepository tutorsRepository;
    private final TimeManagerService timeManagerService;

    Logger logger = LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationRepository reservationRepository, CalendarConfig calendarConfig, StudentService studentService, TutorsRepository tutorsRepository, TimeManagerService timeManagerService) {
        this.reservationRepository = reservationRepository;
        this.calendarConfig = calendarConfig;
        this.studentService = studentService;
        this.tutorsRepository = tutorsRepository;
        this.timeManagerService = timeManagerService;
    }

    @GetMapping("/reservation/{reservationId}")
    public Reservation retrieveReservationById(@PathVariable int reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResourceNotFound("There is no reservation with id: " + reservationId)
        );
    }

    @GetMapping("/google/tutor/{tutorId}/calendar/{calendarId}")
    public List<Event> getCalendarEventsFromCalendarByIds(@PathVariable int tutorId, @PathVariable String calendarId)
            throws GeneralSecurityException, IOException {
        return calendarConfig.getEventsFromCalendar(tutorId, calendarId);
    }

    // Display free time
    @GetMapping("/tutor/{tutorId}/calendar/{calendarId}/{minutesForLesson}")
    public HashMap<LocalDate, List<AvailableTime>> getFreeTime(@PathVariable int tutorId, @PathVariable String calendarId, @PathVariable int minutesForLesson) {
        return timeManagerService.getFreeTime(tutorId, calendarId, minutesForLesson);
    }

    @GetMapping
    public List<Reservation> retrieveAllReservationsForStudent(@RequestParam String email) {
        return reservationRepository.findAllByStudentEmail(email);
    }

    @PostMapping
    public ResponseEntity<Reservation> createNewReservation(@Valid @RequestBody Reservation reservation) {
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
    public void deleteReservationById(@PathVariable int id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFound("There is no student with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @PostMapping("/tutor/{tutorId}/calendar/{calendarId}")
    public void addReservationToCalendar(@PathVariable int tutorId, @PathVariable String calendarId, @RequestBody ShortEvent shortEvent)
            throws GeneralSecurityException, IOException {

        Event event = new Event().setSummary(shortEvent.getSummary());
        DateTime startDateTime = new DateTime(shortEvent.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Poland");
        event.setStart(start);

        DateTime endDateTime = new DateTime(shortEvent.getEnd());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Poland");
        event.setEnd(end);

        List<EventAttendee> attendees = new ArrayList<>();
        attendees.add(new EventAttendee().setEmail(shortEvent.getAttendee()));
        event.setAttendees(attendees);

        Student student = studentService.findStudentByEmailOrThrowException(shortEvent.getAttendee());
        Tutor tutor = tutorsRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFound("There is no tutor with id: " + tutorId));

        Event googleEvent = calendarConfig.addEventToCalendar(tutorId, event, calendarId);
        String htmlLink = googleEvent.getHtmlLink();
        logger.info("Link: {}", htmlLink);

        Reservation reservation = new Reservation(googleEvent.getId(), shortEvent.getStart(), shortEvent.getEnd(), event.getSummary(), student, tutor);
        reservationRepository.save(reservation);
    }

//    @PutMapping("/calendar/{calendarId}/event/{eventId}")
//    public void updateEvent(@PathVariable String calendarId, @PathVariable String eventId)
//            throws GeneralSecurityException, IOException {
//        System.out.printf("Updating event %s\n", eventId);
//
//        EventDateTime start = new EventDateTime()
//                .setDateTime(new DateTime("2023-06-24T14:00:00-07:00"))
//                .setTimeZone("Poland");
//        EventDateTime end = new EventDateTime()
//                .setDateTime(new DateTime("2023-06-24T16:00:00-07:00"))
//                .setTimeZone("Poland");
//
//        calendarConfig.editEventById(1, calendarId, eventId, start, end);
//    }

    @GetMapping("/tutor/{tutorId}/calendar/{calendarId}/event/{eventId}")
    public Event getEvent(@PathVariable String calendarId,
                          @PathVariable int tutorId,
                          @PathVariable String eventId)
            throws GeneralSecurityException, IOException {
        System.out.printf("Getting event %s\n", eventId);
        return calendarConfig.getEventById(tutorId, calendarId, eventId);
    }

    @GetMapping("/tutor/{tutorId}/calendar/{calendarId}/student/{studentMail}")
    public void synchronizeGoogleCalendarWithReservations(@PathVariable String calendarId,
                                                          @PathVariable int tutorId,
                                                          @PathVariable String studentMail) throws GeneralSecurityException, IOException {
        List<Event> eventsFromCalendarForStudent = calendarConfig.getEventsFromCalendarForStudent(tutorId, calendarId, studentMail);
        List<Reservation> allByStudentEmail = reservationRepository.findAllByStudentEmail(studentMail);

        if (eventsFromCalendarForStudent == null) {
            logger.warn("Nothing to update");
            return;
        }

        for (Event calendarEvent : eventsFromCalendarForStudent) {
            String eventId = calendarEvent.getId();
            if (allByStudentEmail.stream().anyMatch(reservation -> reservation.getId().equals(eventId))) {
                EventDateTime start = calendarEvent.getStart();
                EventDateTime end = calendarEvent.getEnd();
                Reservation reservation = reservationRepository.findById(eventId);
                reservation.setStart(new Date(start.getDateTime().getValue()));
                reservation.setEnd(new Date(end.getDateTime().getValue()));
                reservationRepository.save(reservation);
            } else {
                EventDateTime start = calendarEvent.getStart();
                EventDateTime end = calendarEvent.getEnd();
                String summary = calendarEvent.getSummary();
                Student student = studentService.findStudentByEmailOrThrowException(studentMail);
                Tutor tutor = tutorsRepository.findById(tutorId).orElseThrow();
                Reservation reservation = new Reservation(eventId, new Date(start.getDateTime().getValue()), new Date(end.getDateTime().getValue()), summary, student, tutor);
                reservationRepository.save(reservation);
            }
        }
    }

    @GetMapping("/tutor/{tutorId}/calendar/{calendarId}")
    public void synchronizeGoogleCalendarWithReservations(@PathVariable String calendarId,
                                                          @PathVariable int tutorId) throws GeneralSecurityException, IOException {
        List<Event> googleReservations = calendarConfig.getEventsFromCalendar(tutorId, calendarId);
        List<Reservation> dbReservations = reservationRepository.findAllByTutorId(tutorId);

        if (googleReservations == null) {
            logger.warn("Nothing to update");
            return;
        }

        for (Event calendarEvent : googleReservations) {
            String eventId = calendarEvent.getId();
            if (dbReservations.stream().anyMatch(reservation -> reservation.getId().equals(eventId))) {
                EventDateTime start = calendarEvent.getStart();
                EventDateTime end = calendarEvent.getEnd();
                Reservation reservation = reservationRepository.findById(eventId);
                reservation.setStart(new Date(start.getDateTime().getValue()));
                reservation.setEnd(new Date(end.getDateTime().getValue()));
                reservationRepository.save(reservation);
            } else {
                EventDateTime start = calendarEvent.getStart();
                EventDateTime end = calendarEvent.getEnd();
                String summary = calendarEvent.getSummary();
                List<EventAttendee> attendees = calendarEvent.getAttendees();
                Optional<String> studentMail;
                if (attendees == null) {
                    continue;
                }
                List<Student> allStudents = studentService.getAllStudents();
                List<String> emailStudents = allStudents.stream().map(Student::getEmail).toList();
                studentMail = attendees
                        .stream()
                        .filter(attendee -> emailStudents.contains(attendee.getEmail())).findFirst().map(EventAttendee::getEmail);
                Student student;
                if (studentMail.isPresent()) {
                    student = studentService.findStudentByEmailOrThrowException(studentMail.get());
                } else {
                    continue;
                }
                Tutor tutor = tutorsRepository.findById(tutorId).orElseThrow();
                Reservation reservation = new Reservation(eventId, new Date(start.getDateTime().getValue()), new Date(end.getDateTime().getValue()), summary, student, tutor);
                reservationRepository.save(reservation);
            }
        }
    }

}
