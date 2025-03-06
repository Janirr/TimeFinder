package com.poznan.put.rest.webservice.restapi.controllers;

import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarConfig calendarConfig;

    public CalendarController(CalendarConfig calendarConfig) {
        this.calendarConfig = calendarConfig;
    }

    @GetMapping("/authorize/{tutorId}")
    public Map<String, String> authorizeTutor(@PathVariable Integer tutorId) throws GeneralSecurityException, IOException {
        String authURL = CalendarConfig.getAuthorizationURL(tutorId);
        return Map.of("authUrl", authURL);
    }
}
