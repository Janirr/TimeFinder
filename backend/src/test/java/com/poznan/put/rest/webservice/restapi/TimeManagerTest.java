package com.poznan.put.rest.webservice.restapi;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.model.Event;
import com.poznan.put.rest.webservice.restapi.calendar.AvailableTime;
import com.poznan.put.rest.webservice.restapi.calendar.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.calendar.TimeManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TimeManagerTest {
    private TimeManager timeManager;

    @BeforeEach
    public void setUp() {
        timeManager = new TimeManager();
    }

    @Test
    public void testGetNextDays() {
        int numberOfDays = 7;
        int expectedSize = numberOfDays;
        Date[] nextDays = timeManager.getNextDays(numberOfDays);
        Assertions.assertEquals(expectedSize, nextDays.length);
    }

//    @Test
//    public void testGetFreeTimeDefaultTutor() throws GeneralSecurityException, IOException {
//        int tutorId = 1;
//        String calendarId = "c0cc6a538c4604e5570b325de0095a2e9c1647adfc9c4e5f7bbc5efb71c5db57@group.calendar.google.com";
//        int minutesForLesson = 60;
//
//        ArrayList<ArrayList<AvailableTime>> availableTimes = timeManager.getFreeTime(tutorId, calendarId, minutesForLesson);
//
//        // Perform assertions on the generated availableTimes
//        // Assert that the availableTimes is not null
//        Assertions.assertNotNull(availableTimes);
//
//        // Assert that the size of availableTimes is correct
//        int expectedNumberOfDays = 14;
//        Assertions.assertEquals(expectedNumberOfDays, availableTimes.size());
//
//        // Perform more specific assertions on the availableTimes if needed
//        // ...
//
//        // Example assertion on the first day's available times
//        ArrayList<AvailableTime> firstDayAvailableTimes = availableTimes.get(0);
//        Assertions.assertNotNull(firstDayAvailableTimes);
//
//        // Assert that there is at least one available time on the first day
//        Assertions.assertTrue(firstDayAvailableTimes.size() > 0);
//    }

    @Test
    public void testGetEventsFromCalendarById_CalendarNotFound() throws GeneralSecurityException, IOException {
        String calendarId = "nonExistentCalendarId";
        int tutorId = 1;
        Assertions.assertThrows(IOException.class, () -> {
            timeManager.getFreeTime(tutorId, calendarId, 60);
        });
    }
}
