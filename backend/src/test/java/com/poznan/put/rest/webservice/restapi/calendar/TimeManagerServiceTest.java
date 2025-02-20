package com.poznan.put.rest.webservice.restapi.calendar;

import com.poznan.put.rest.webservice.restapi.model.records.AvailableTime;
import com.poznan.put.rest.webservice.restapi.model.records.Timestamp;
import com.poznan.put.rest.webservice.restapi.services.TimeManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TimeManagerServiceTest {
    public static final int MINUTES_FOR_LESSON = 60;
    public static final int MINUTES_TO_ADD = 15;
    @Mock
    private TimeManagerService timeManagerService;

    @Test
    void shouldNotGenerateAnyFreeTimestamps() {
        ArrayList<AvailableTime> actualFreeTimes = new ArrayList<>();
        timeManagerService.generateFreeTimestamps(MINUTES_FOR_LESSON, actualFreeTimes, LocalTime.of(16, 0), LocalTime.of(16, 59));
        assertEquals(Collections.emptyList(), actualFreeTimes);
    }

    @Test
    void shouldGenerateOneFreeTimestamps() {
        ArrayList<AvailableTime> expectedFreeTimes = new ArrayList<>();
        LocalTime startHour = LocalTime.of(16, 0);
        LocalTime endHour = startHour.plusMinutes(MINUTES_FOR_LESSON);
        for (int i = 0; i < 1; i++) {
            expectedFreeTimes.add(new AvailableTime(startHour, endHour));
            startHour = startHour.plusMinutes(MINUTES_TO_ADD);
            endHour = endHour.plusMinutes(MINUTES_TO_ADD);
        }

        ArrayList<AvailableTime> actualFreeTimes = new ArrayList<>();
        timeManagerService.generateFreeTimestamps(MINUTES_FOR_LESSON, actualFreeTimes, LocalTime.of(16, 0), LocalTime.of(17, 0));
        assertEquals(expectedFreeTimes, actualFreeTimes);
    }

    @Test
    void shouldGenerateFiveFreeTimestamps() {
        ArrayList<AvailableTime> expectedFreeTimes = new ArrayList<>();
        LocalTime startHour = LocalTime.of(16, 0);
        LocalTime endHour = startHour.plusMinutes(MINUTES_FOR_LESSON);
        for (int i = 0; i < 5; i++) {
            expectedFreeTimes.add(new AvailableTime(startHour, endHour));
            startHour = startHour.plusMinutes(MINUTES_TO_ADD);
            endHour = endHour.plusMinutes(MINUTES_TO_ADD);
        }

        ArrayList<AvailableTime> actualFreeTimes = new ArrayList<>();
        timeManagerService.generateFreeTimestamps(MINUTES_FOR_LESSON, actualFreeTimes, LocalTime.of(16, 0), LocalTime.of(18, 0));
        assertEquals(expectedFreeTimes, actualFreeTimes);
    }

    @Test
    void shouldGenerateAvailableTimes() {
        HashMap<LocalDate, List<AvailableTime>> result = new HashMap<>();
        Date[] dates = new Date[1];
        Date today = Date.from(Instant.now());
        dates[0] = today;

        HashMap<LocalDate, List<Timestamp>> hashmapTakenTimes = new HashMap<>();
        List<Timestamp> takenTimes = new ArrayList<>();
        takenTimes.add(new Timestamp(LocalTime.of(10, 0), LocalTime.of(14, 0)));
        takenTimes.add(new Timestamp(LocalTime.of(15, 0), LocalTime.of(16, 30)));
        takenTimes.add(new Timestamp(LocalTime.of(16, 30), LocalTime.of(16, 45)));
        takenTimes.sort(Comparator.comparing(Timestamp::startHour));
        hashmapTakenTimes.put(TimeManagerService.getLocalDateFromDate(today), takenTimes);
        timeManagerService.setTakenTimes(hashmapTakenTimes);

        HashMap<LocalDate, List<Timestamp>> hashMapMarkedFreeTimes = new HashMap<>();
        List<Timestamp> markedFreeTimes = new ArrayList<>();
        markedFreeTimes.add(new Timestamp(LocalTime.of(8, 0), LocalTime.of(17, 30)));
        markedFreeTimes.sort(Comparator.comparing(Timestamp::startHour));
        hashMapMarkedFreeTimes.put(TimeManagerService.getLocalDateFromDate(today), markedFreeTimes);
        timeManagerService.setMarkedFreeTimes(hashMapMarkedFreeTimes);

        timeManagerService.generateAvailableTimes(MINUTES_FOR_LESSON, result, dates);

        assertEquals(6, result.get(LocalDate.now()).size());
    }
}