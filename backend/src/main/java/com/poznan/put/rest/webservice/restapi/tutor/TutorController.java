package com.poznan.put.rest.webservice.restapi.tutor;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.calendar.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.PricingRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.pricing.Pricing;
import com.poznan.put.rest.webservice.restapi.pricing.PricingRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tutors")
public class TutorController {
    private final TutorsRepository tutorsRepository;
    private final CalendarConfig calendarConfig;
    private final PricingRepository pricingRepository;

    public TutorController(TutorsRepository tutorsRepository, CalendarConfig calendarConfig, PricingRepository pricingRepository) {
        this.tutorsRepository = tutorsRepository;
        this.calendarConfig = calendarConfig;
        this.pricingRepository = pricingRepository;
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
        return calendarConfig.getEventsFromCalendar(Tutor.get().getId(), "primary");
    }

    @GetMapping("/{id}/calendar/{calendarId}")
    public List<Event> getTutorCustomCalendarById(@PathVariable Long id, @PathVariable String calendarId) throws GeneralSecurityException, IOException {
        Optional<Tutor> tutor = tutorsRepository.findById(id);

        if (tutor.isEmpty()) {
            throw new ResourceNotFound("There is no tutor with id: " + id);
        }
        return calendarConfig.getEventsFromCalendar(tutor.get().getId(), calendarId);
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
            events.add(calendarConfig.getEventsFromCalendar(tutor.get().getId(), calendarId));
        }

        return events;
    }

    @GetMapping("/subject/{subject}")
    public Tutor getTutorBySubject(@PathVariable String subject) {
        return tutorsRepository.findBySubject(subject).orElseThrow();
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

    @GetMapping("/{id}/pricings")
    public List<Pricing> getPricingsForTutor(@PathVariable Long id) {
        Optional<Tutor> tutor = tutorsRepository.findById(id);
        return tutor.map(pricingRepository::findAllByTutor).orElse(null);
    }

    @PostMapping("/{id}/pricings")
    @Transactional
    public void updatePricingsForTutor(@PathVariable Long id, @RequestBody List<PricingRequest> pricings) {
        Optional<Tutor> tutor = tutorsRepository.findById(id);
        List<Pricing> pricingArrayList = new ArrayList<>();

        if (tutor.isEmpty()) {
            return;
        }

        for (PricingRequest pricingRequest : pricings) {
            Pricing pricing = new Pricing();
            pricing.setLevel(pricingRequest.getLevel());
            pricing.setPrice(pricingRequest.getPrice());
            pricing.setTutor(tutor.get());
            pricingArrayList.add(pricing);
        }
        pricingRepository.deleteAllByTutor(tutor.get());
        pricingRepository.saveAll(pricingArrayList);
    }
}
