package com.poznan.put.rest.webservice.restapi.services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.controllers.responses.AvailableTimeResponse;
import com.poznan.put.rest.webservice.restapi.jpa.ReservationRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.Reservation;
import com.poznan.put.rest.webservice.restapi.jpa.model.Student;
import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
import com.poznan.put.rest.webservice.restapi.services.helpers.ShortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CalendarConfig calendarConfig;
    private final StudentService studentService;
    private final TutorService tutorService;
    private final TimeManagerService timeManagerService;

    Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, CalendarConfig calendarConfig, StudentService studentService, TutorService tutorService, TimeManagerService timeManagerService) {
        this.reservationRepository = reservationRepository;
        this.calendarConfig = calendarConfig;
        this.studentService = studentService;
        this.tutorService = tutorService;
        this.timeManagerService = timeManagerService;
    }

    public Reservation getReservationById(int reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new RuntimeException("Reservation not found with id: " + reservationId)
        );
    }

    public List<Event> getCalendarEventsFromCalendar(int tutorId) throws GeneralSecurityException, IOException {
        Tutor tutor = tutorService.getTutorById((long) tutorId);
        return calendarConfig.getEventsFromCalendar(tutorId, tutor.getCalendarId());
    }

    public HashMap<LocalDate, List<AvailableTimeResponse>> getFreeTime(int tutorId, int minutesForLesson) {
        return timeManagerService.getFreeTime(tutorId, minutesForLesson);
    }

    public List<Reservation> getAllReservationsForStudent(String studentEmail) {
        return reservationRepository.findAllByStudentEmail(studentEmail);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservationById(int id) {
        reservationRepository.deleteById(id);
    }

    public void addReservationToCalendar(int tutorId, ShortEvent shortEvent) throws GeneralSecurityException, IOException {
        Event event = createEventFromShortEvent(shortEvent);

        Student student = studentService.findStudentByEmailOrThrowException(shortEvent.getAttendee());
        Tutor tutor = tutorService.getTutorById((long) tutorId);

        Event googleEvent = calendarConfig.addEventToCalendar(tutorId, event, tutor.getCalendarId());
        String htmlLink = googleEvent.getHtmlLink();
        logger.info("Link: {}", htmlLink);

        Reservation reservation = new Reservation(googleEvent.getId(), shortEvent.getStart(), shortEvent.getEnd(), event.getSummary(), student, tutor);
        reservationRepository.save(reservation);
    }

    public Event getEventById(int tutorId, String eventId) throws GeneralSecurityException, IOException {
        String calendarId = tutorService.getTutorById((long) tutorId).getCalendarId();
        return calendarConfig.getEventById(tutorId, eventId, calendarId);
    }

    public void synchronizeGoogleCalendarWithReservations(int tutorId) throws GeneralSecurityException, IOException {
        Tutor tutorById = tutorService.getTutorById((long) tutorId);
        List<Event> googleReservations = calendarConfig.getEventsFromCalendar(tutorId, tutorById.getCalendarId());
        List<Reservation> dbReservations = reservationRepository.findAllByTutorId(tutorId);

        if (googleReservations == null) {
            logger.warn("Nothing to update");
            return;
        }

        for (Event calendarEvent : googleReservations) {
            synchronizeReservationWithGoogleEvent(calendarEvent, dbReservations, tutorId);
        }
    }

    private void synchronizeReservationWithGoogleEvent(Event calendarEvent, List<Reservation> dbReservations, int tutorId) {
        String eventId = calendarEvent.getId();
        if (dbReservations.stream().anyMatch(reservation -> reservation.getId().equals(eventId))) {
            updateReservationFromGoogleEvent(calendarEvent, eventId);
        } else {
            createReservationFromGoogleEvent(calendarEvent, tutorId);
        }
    }

    private void updateReservationFromGoogleEvent(Event calendarEvent, String eventId) {
        EventDateTime start = calendarEvent.getStart();
        EventDateTime end = calendarEvent.getEnd();
        Reservation reservation = reservationRepository.findById(eventId);
        reservation.setStart(new Date(start.getDateTime().getValue()));
        reservation.setEnd(new Date(end.getDateTime().getValue()));
        reservationRepository.save(reservation);
    }

    private void createReservationFromGoogleEvent(Event calendarEvent, int tutorId) {
        EventDateTime start = calendarEvent.getStart();
        EventDateTime end = calendarEvent.getEnd();
        String summary = calendarEvent.getSummary();
        List<EventAttendee> attendees = calendarEvent.getAttendees();

        if (attendees == null) return;

        String studentEmail = getStudentEmailFromAttendees(attendees);
        Student student = studentService.findStudentByEmailOrThrowException(studentEmail);
        Tutor tutor = tutorService.getTutorById((long) tutorId);
        Reservation reservation = new Reservation(calendarEvent.getId(), new Date(start.getDateTime().getValue()), new Date(end.getDateTime().getValue()), summary, student, tutor);
        reservationRepository.save(reservation);
    }

    private String getStudentEmailFromAttendees(List<EventAttendee> attendees) {
        List<Student> allStudents = studentService.getAllStudents();
        List<String> emailStudents = allStudents.stream().map(Student::getEmail).toList();
        return attendees.stream()
                .filter(attendee -> emailStudents.contains(attendee.getEmail()))
                .findFirst()
                .map(EventAttendee::getEmail)
                .orElse(null);
    }

    private Event createEventFromShortEvent(ShortEvent shortEvent) {
        Event event = new Event().setSummary(shortEvent.getSummary());
        DateTime startDateTime = new DateTime(shortEvent.getStart());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Poland");
        event.setStart(start);

        DateTime endDateTime = new DateTime(shortEvent.getEnd());
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Poland");
        event.setEnd(end);

        List<EventAttendee> attendees = new ArrayList<>();
        attendees.add(new EventAttendee().setEmail(shortEvent.getAttendee()));
        event.setAttendees(attendees);

        return event;
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
