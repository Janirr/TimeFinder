package com.poznan.put.rest.webservice.restapi.services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.controllers.responses.AvailableTimeResponse;
import com.poznan.put.rest.webservice.restapi.jpa.ReservationRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
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
    private final TutorsRepository tutorsRepository;
    private final TimeManagerService timeManagerService;

    Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, CalendarConfig calendarConfig,
                              StudentService studentService, TutorsRepository tutorsRepository,
                              TimeManagerService timeManagerService) {
        this.reservationRepository = reservationRepository;
        this.calendarConfig = calendarConfig;
        this.studentService = studentService;
        this.tutorsRepository = tutorsRepository;
        this.timeManagerService = timeManagerService;
    }

    public Reservation getReservationById(int reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new RuntimeException("Reservation not found with id: " + reservationId)
        );
    }

    public List<Event> getCalendarEventsFromCalendar(int tutorId, String calendarId) throws GeneralSecurityException, IOException {
        return calendarConfig.getEventsFromCalendar(tutorId, calendarId);
    }

    public HashMap<LocalDate, List<AvailableTimeResponse>> getFreeTime(int tutorId, String calendarId, int minutesForLesson) {
        return timeManagerService.getFreeTime(tutorId, calendarId, minutesForLesson);
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

    public void addReservationToCalendar(int tutorId, String calendarId, ShortEvent shortEvent) throws GeneralSecurityException, IOException {
        Event event = createEventFromShortEvent(shortEvent);

        Student student = studentService.findStudentByEmailOrThrowException(shortEvent.getAttendee());
        Tutor tutor = tutorsRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("There is no tutor with id: " + tutorId));

        Event googleEvent = calendarConfig.addEventToCalendar(tutorId, event, calendarId);
        String htmlLink = googleEvent.getHtmlLink();
        logger.info("Link: {}", htmlLink);

        Reservation reservation = new Reservation(googleEvent.getId(), shortEvent.getStart(), shortEvent.getEnd(), event.getSummary(), student, tutor);
        reservationRepository.save(reservation);
    }

    public Event getEventById(int tutorId, String calendarId, String eventId) throws GeneralSecurityException, IOException {
        return calendarConfig.getEventById(tutorId, calendarId, eventId);
    }

    public void synchronizeGoogleCalendarWithReservations(String calendarId, int tutorId) throws GeneralSecurityException, IOException {
        List<Event> googleReservations = calendarConfig.getEventsFromCalendar(tutorId, calendarId);
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
        Tutor tutor = tutorsRepository.findById(tutorId).orElseThrow();
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
}
