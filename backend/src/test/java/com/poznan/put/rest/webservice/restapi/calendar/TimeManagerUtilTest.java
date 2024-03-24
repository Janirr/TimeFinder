package com.poznan.put.rest.webservice.restapi.calendar;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


class TimeManagerTest {

    public static final int MINUTES_FOR_LESSON = 60;
    public static final int MINUTES_TO_ADD = 15;

    @Test
    void getFreeTime() {
    }

    @Test
    void generateFreeTimestamps() {
    }

    @Test
    void generateNextDayTimestamps() {
        ArrayList<AvailableTime> markedFreeTimesInDay = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 8, 27);
        for (int i = 0; i < 14; i++) {
            date = date.plusDays(1);
            LocalTime takenStartHour = LocalTime.of(15, 0);
            LocalTime takenEndHour = LocalTime.of(16, 30);

            LocalTime freeStartHour = LocalTime.of(14, 0);
            LocalTime freeEndHour = LocalTime.of(20, 0);

            while (freeStartHour.plusMinutes(MINUTES_FOR_LESSON).isBefore(freeEndHour)) {
                LocalTime possibleEndHour = freeStartHour.plusMinutes(MINUTES_FOR_LESSON);
                if (isBetween(takenStartHour, takenEndHour, possibleEndHour) && isBetween(takenStartHour, takenEndHour, freeStartHour)) {
                    markedFreeTimesInDay.add(new AvailableTime(freeStartHour, possibleEndHour));
                }
                freeStartHour = freeStartHour.plusMinutes(MINUTES_TO_ADD);
            }
        }
        System.out.println(markedFreeTimesInDay);
    }

    private boolean isBetween(LocalTime start, LocalTime end, LocalTime time) {
        return time.isBefore(start) || time.isAfter(end);
    }
}