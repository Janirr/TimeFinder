package com.poznan.put.rest.webservice.restapi.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
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
import com.google.api.services.calendar.model.*;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration
public class CalendarConfig {
    static final String APPLICATION_NAME = "TimeFinder";
    static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    static final String TOKENS_DIRECTORY_PATH = "tokens";
    static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, int tutorId)
            throws IOException {
        // Load client secrets.
        InputStream in = Calendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(String.valueOf(tutorId));
    }

    static Calendar getAuthorization(int tutorId)
            throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, tutorId))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        getAuthorization(1);
    }

    public List<Event> getEventsFromCalendarById(int tutorId, String calendarId) throws GeneralSecurityException, IOException {
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

    public void addEventToCalendar(int tutorId, Event event, String calendarId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        service.events().insert(calendarId, event).execute();
    }

    public void editEventById(int tutorId, String calendarId, String eventId, EventDateTime start, EventDateTime end)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);
        Event event = service.events().get(calendarId, eventId).execute();
        event.setEnd(end);
        event.setStart(start);
        Event updatedEvent = service.events().update(calendarId, eventId, event).execute();
        System.out.println(updatedEvent.getUpdated());
    }

    public void deleteEventById(int tutorId, String calendarId, String eventId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        service.events().delete(calendarId, eventId).execute();
    }

    public void deleteCalendarById(int tutorId, String calendarId)
            throws GeneralSecurityException, IOException {
        Calendar service = getAuthorization(tutorId);

        service.calendars().delete(calendarId).execute();
    }

}
