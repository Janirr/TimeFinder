package com.poznan.put.rest.webservice.restapi.configuration;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.poznan.put.rest.webservice.restapi.exception.OAuthUnauthorizedException;
import com.poznan.put.rest.webservice.restapi.jpa.OAuthCredentialRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.OAuthCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
public class CalendarConfig {
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    static final String APPLICATION_NAME = "TimeFinder";
    static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final Logger log = LoggerFactory.getLogger(CalendarConfig.class);
    private final OAuthCredentialRepository credentialRepository;

    public CalendarConfig(OAuthCredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public static String getAuthorizationURL(int tutorId) throws GeneralSecurityException, IOException {
        // Load client secrets
        InputStream in = CalendarConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and create authorization URL
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        String state = Base64.getEncoder().encodeToString(String.valueOf(tutorId).getBytes());

        return flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/oauth-callback")
                .setState(state)
                .build();
    }

    public Calendar getAuthorization(int tutorId) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Fetch credentials from the database
        Optional<OAuthCredential> storedCredentials = credentialRepository.findByTutorId(tutorId);

        log.info("Please authorize at {}", getAuthorizationURL(tutorId));
        storedCredentials.orElseThrow(() -> new OAuthUnauthorizedException("Could not find credentials for tutorId: " + tutorId));
        OAuthCredential oAuthCredential = storedCredentials.get();

        // Create Credential from the stored OAuthCredential object
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(oAuthCredential.getAccessToken())
                .setRefreshToken(oAuthCredential.getRefreshToken())
                .setExpirationTimeMilliseconds(oAuthCredential.getExpirationTimeMilliseconds());

        // Check if the token has expired, and refresh if necessary
        if (credential.getAccessToken() != null && credential.getExpirationTimeMilliseconds() < System.currentTimeMillis()) {
            credential.refreshToken(); // Refresh the token if expired
        }

        // Build the Calendar service using the valid credentials
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<Event> getEventsFromCalendar(int tutorId, String calendarId) throws GeneralSecurityException, IOException {
        try {
            // Build a new authorized API client service.
            Calendar service = getAuthorization(tutorId);

            // Set the timeMin to current time and timeMax to 2 weeks from current time
            LocalDateTime now = LocalDateTime.now();
            DateTime timeMin = new DateTime(System.currentTimeMillis());
            DateTime timeMax = new DateTime(now.plusWeeks(2).toString());

            Events events = service.events().list(calendarId)
                    .setTimeMin(timeMin)
                    .setTimeMax(timeMax)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .setPrettyPrint(true)
                    .execute();

            return events.getItems();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                // Handle 404 error here
                throw new IOException("Calendar not found: " + calendarId);
            } else {
                throw new IOException("Error retrieving events from calendar: " + e.getMessage());
            }
        }

    }


    public List<CalendarListEntry> getAllCalendarsForTutor(int tutorId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        // Iterate through entries in calendar list
        CalendarList calendarList = service
                .calendarList()
                .list()
                .setFields("items(id,summary)")
                .execute();
        return calendarList.getItems();
    }

    public Event getEventById(int tutorId, String calendarId, String eventId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        return service.events().get(calendarId, eventId).execute();
    }

    public Event addEventToCalendar(int tutorId, Event event, String calendarId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        return service.events().insert(calendarId, event).execute();
    }
}
