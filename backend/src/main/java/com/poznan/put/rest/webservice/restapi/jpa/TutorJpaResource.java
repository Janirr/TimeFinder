package com.poznan.put.rest.webservice.restapi.jpa;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.calendar.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tutors")
public class TutorJpaResource {
    private final TutorsRepository tutorsRepository;
    private final CalendarConfig calendarConfig;

    public TutorJpaResource(TutorsRepository tutorsRepository, CalendarConfig calendarConfig) {
        this.tutorsRepository = tutorsRepository;
        this.calendarConfig = calendarConfig;
    }

    @GetMapping
    public List<Tutor> getAllTutors() {
        return tutorsRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Tutor> getTutorById(@PathVariable Long id) {
        Optional<Tutor> Tutor = tutorsRepository.findById(id);
        if (Tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }
        return Tutor;
    }

    @GetMapping("/{id}/calendar")
    public List<Event> getTutorPrimaryCalendarById(@PathVariable Long id) throws GeneralSecurityException, IOException {
        Optional<Tutor> Tutor = tutorsRepository.findById(id);

        if (Tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }
        return calendarConfig.getEventsFromCalendarById(Tutor.get().getId(), "primary");
    }

    @GetMapping("/{id}/calendar/{calendarId}")
    public List<Event> getTutorCustomCalendarById(@PathVariable Long id, @PathVariable String calendarId) throws GeneralSecurityException, IOException {
        Optional<Tutor> tutor = tutorsRepository.findById(id);

        if (tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }
        return calendarConfig.getEventsFromCalendarById(tutor.get().getId(), calendarId);
    }

    @GetMapping("/{id}/calendars")
    public List<CalendarListEntry> getTutorCalendars(@PathVariable Long id) throws GeneralSecurityException, IOException {
        Optional<Tutor> tutor = tutorsRepository.findById(id);

        if (tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }
        return calendarConfig.getAllCalendarsForTutor(tutor.get().getId());
    }

    @GetMapping("/{id}/calendars/events")
    public List<List<Event>> getTutorCalendarEvents(@PathVariable Long id) throws GeneralSecurityException, IOException {
        Optional<Tutor> tutor = tutorsRepository.findById(id);

        if (tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }

        List<List<Event>> events = new ArrayList<>();
        List<CalendarListEntry> calendars = calendarConfig.getAllCalendarsForTutor(tutor.get().getId());

        for (CalendarListEntry calendar : calendars) {
            String calendarId = calendar.getId();
            events.add(calendarConfig.getEventsFromCalendarById(tutor.get().getId(), calendarId));
        }

        return events;
    }

    @GetMapping("/subject/{subject}")
    public Tutor getTutorBySubject(@PathVariable String subject) {
        return tutorsRepository.findBySubject(subject);
    }

    @PutMapping("{tutorId}/calendar/{calendarId}")
    public void updateTutorCalendar(@PathVariable Long tutorId, @PathVariable String calendarId) {
        Optional<Tutor> tutorOptional = tutorsRepository.findById(tutorId);

        if (tutorOptional.isEmpty()) {
            throw new ResourceNotFound("There is no tutorOptional with id: " + tutorId);
        }

        Tutor tutor = tutorOptional.get();
        tutor.setCalendarId(calendarId);
        tutorsRepository.save(tutor);
    }
}
