package com.poznan.put.rest.webservice.restapi.services;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.controllers.requests.PricingRequest;
import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.Pricing;
import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService {
    private final TutorsRepository tutorsRepository;
    private final CalendarConfig calendarConfig;
    private final PricingService pricingService;

    public TutorService(TutorsRepository tutorsRepository, CalendarConfig calendarConfig, PricingService pricingService) {
        this.tutorsRepository = tutorsRepository;
        this.calendarConfig = calendarConfig;
        this.pricingService = pricingService;
    }

    public List<Tutor> getAllTutors() {
        return tutorsRepository.findAll();
    }

    public Tutor getTutorById(Long id) {
        return tutorsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("There is no tutor with id: " + id));
    }

    public List<Event> getTutorCalendarEvents(Long id) throws GeneralSecurityException, IOException {
        Tutor tutor = getTutorById(id);
        return calendarConfig.getEventsFromCalendar(tutor.getId(), tutor.getCalendarId());
    }

    public List<CalendarListEntry> getTutorCalendars(Long id) throws GeneralSecurityException, IOException {
        Tutor tutor = getTutorById(id);
        return calendarConfig.getAllCalendarsForTutor(tutor.getId());
    }

    public List<List<Event>> getTutorCalendarsEvents(Long id) throws GeneralSecurityException, IOException {
        Tutor tutor = getTutorById(id);
        List<List<Event>> events = new ArrayList<>();
        List<CalendarListEntry> calendars = calendarConfig.getAllCalendarsForTutor(tutor.getId());
        for (CalendarListEntry calendar : calendars) {
            events.add(calendarConfig.getEventsFromCalendar(tutor.getId(), tutor.getCalendarId()));
        }
        return events;
    }

    public Tutor getTutorBySubject(String subject) {
        return tutorsRepository.findBySubject(subject)
                .orElseThrow(() -> new ResourceNotFound("No tutor found for subject: " + subject));
    }

    public void updateTutorCalendar(Long tutorId, String calendarId) {
        Tutor tutor = getTutorById(tutorId);
        tutor.setCalendarId(calendarId);
        tutorsRepository.save(tutor);
    }

    public List<Pricing> getPricingsForTutor(Long id) {
        Tutor tutor = getTutorById(id);
        return pricingService.findAllByTutor(tutor);
    }

    @Transactional
    public void updatePricingsForTutor(Long id, List<PricingRequest> pricings) {
        Tutor tutor = getTutorById(id);
        List<Pricing> pricingArrayList = new ArrayList<>();
        for (PricingRequest pricingRequest : pricings) {
            Pricing pricing = new Pricing();
            pricing.setLevel(pricingRequest.level());
            pricing.setPrice(pricingRequest.price());
            pricing.setTutor(tutor);
            pricingArrayList.add(pricing);
        }
        pricingService.deleteAllByTutor(tutor);
        pricingService.saveAll(pricingArrayList);
    }

    public Optional<Tutor> findByEmail(String email) {
        return tutorsRepository.findByEmail(email);
    }

    public Optional<Tutor> findByPhoneNumber(String phoneNumber) {
        return tutorsRepository.findByPhoneNumber(phoneNumber);
    }

    public Tutor save(Tutor tutor) {
        return tutorsRepository.save(tutor);
    }

    public Optional<Tutor> findByEmailAndPassword(String email, String password) {
        return tutorsRepository.findByEmailAndPassword(email, password);
    }
}
