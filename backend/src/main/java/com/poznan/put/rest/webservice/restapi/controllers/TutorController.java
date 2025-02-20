package com.poznan.put.rest.webservice.restapi.controllers;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.controllers.requests.PricingRequest;
import com.poznan.put.rest.webservice.restapi.jpa.model.Pricing;
import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
import com.poznan.put.rest.webservice.restapi.services.TutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/tutors")
public class TutorController {
    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public ResponseEntity<List<Tutor>> getAllTutors() {
        return ResponseEntity.ok(tutorService.getAllTutors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutor> getTutorById(@PathVariable Long id) {
        return ResponseEntity.ok(tutorService.getTutorById(id));
    }

    @GetMapping("/{id}/calendar/{calendarId}")
    public ResponseEntity<List<Event>> getTutorCalendar(@PathVariable Long id, @PathVariable String calendarId)
            throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(tutorService.getTutorCalendar(id, calendarId));
    }

    @GetMapping("/{id}/calendars")
    public ResponseEntity<List<CalendarListEntry>> getTutorCalendars(@PathVariable Long id)
            throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(tutorService.getTutorCalendars(id));
    }

    @GetMapping("/{id}/calendars/events")
    public ResponseEntity<List<List<Event>>> getTutorCalendarEvents(@PathVariable Long id)
            throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(tutorService.getTutorCalendarEvents(id));
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<Tutor> getTutorBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(tutorService.getTutorBySubject(subject));
    }

    @PutMapping("/{tutorId}/calendar/{calendarId}")
    public ResponseEntity<Void> updateTutorCalendar(@PathVariable Long tutorId, @PathVariable String calendarId) {
        tutorService.updateTutorCalendar(tutorId, calendarId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pricings")
    public ResponseEntity<List<Pricing>> getPricingsForTutor(@PathVariable Long id) {
        return ResponseEntity.ok(tutorService.getPricingsForTutor(id));
    }

    @PostMapping("/{id}/pricings")
    public ResponseEntity<Void> updatePricingsForTutor(@PathVariable Long id, @RequestBody List<PricingRequest> pricings) {
        tutorService.updatePricingsForTutor(id, pricings);
        return ResponseEntity.noContent().build();
    }
}
