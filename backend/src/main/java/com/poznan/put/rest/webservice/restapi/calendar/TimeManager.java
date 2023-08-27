package com.poznan.put.rest.webservice.restapi.calendar;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * This class is responsible for managing time in the application.
 * It is used to generate free and busy times for the tutor.
 * TODO: - instead of markedFreeTimes and takenTimes use one ArrayList of Length of Days..
 * TODO: ..and then use DayTimestamp to mark free and busy times inside of it.
 * It will be easier this way to check if there is a free time for the lesson.
 */

public class TimeManager {
    public static final int NUMBER_OF_DAYS = 14;
    public static final int MINUTES_TO_ADD = 15;
    private final ArrayList<DayTimestamp> markedFreeTimes = new ArrayList<>();
    private final ArrayList<DayTimestamp> takenTimes = new ArrayList<>();
    private final CalendarConfig calendarConfig = new CalendarConfig();


    public Date[] getNextDays(int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        Date[] days = new Date[numberOfDays];

        for (int day = 0; day < numberOfDays; day++) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, day);
            days[day] = calendar.getTime();
        }
        return days;
    }

    public HashMap<LocalDate, ArrayList<AvailableTime>> getFreeTime(int tutorId, String calendarId, int minutesForLesson) {
        HashMap<LocalDate, ArrayList<AvailableTime>> availableTime = new HashMap<>();
        try {
            Date[] days = getNextDays(NUMBER_OF_DAYS);
            getCalendarEvents(tutorId, calendarId);
            generateAvailableTimes(minutesForLesson, availableTime, days);
        } catch (Exception e) {
            System.out.println("Error in getting free time: " + e.getMessage());
        }
        return availableTime;
    }

    private void generateAvailableTimes(int minutesForLesson, HashMap<LocalDate, ArrayList<AvailableTime>> availableTime, Date[] days) {
        for (Date date : days) {
            ArrayList<AvailableTime> markedFreeTimesInDay = new ArrayList<>();
            LocalDate day = getLocalDateFromDate(date);
            for (DayTimestamp freeEventTimeStamp : markedFreeTimes) {
                if (day.equals(freeEventTimeStamp.date())) {
                    LocalTime possibleStartHour = freeEventTimeStamp.startHour();
                    LocalTime maximumEndHour = freeEventTimeStamp.endHour();
                    for (DayTimestamp takenEventTimeStamp : takenTimes) {
                        LocalTime takenEventStartTime = takenEventTimeStamp.startHour();
                        if (day.equals(takenEventTimeStamp.date())) {
                            if (possibleStartHour != null && takenEventStartTime != null) {
                                generateFreeTimestamps(minutesForLesson, markedFreeTimesInDay, possibleStartHour, takenEventStartTime);
                                possibleStartHour = takenEventTimeStamp.endHour();
                            }
                        }
                    }
                    if (possibleStartHour != null && maximumEndHour != null) {
                        generateFreeTimestamps(minutesForLesson, markedFreeTimesInDay, possibleStartHour, maximumEndHour);
                    }
                }
            }
            availableTime.put(day, markedFreeTimesInDay);
        }
    }

    private void getCalendarEvents(int tutorId, String calendarId) throws GeneralSecurityException, IOException {
        for (Event event : calendarConfig.getEventsFromCalendarById(tutorId, calendarId)) {
            LocalDate eventDay = getLocalDateFromDate(new Date(event.getStart().getDateTime().getValue()));
            LocalTime startHour = getLocalTime(event.getStart());
            LocalTime endHour = getLocalTime(event.getEnd());
            DayTimestamp timestamp = new DayTimestamp(eventDay, startHour, endHour);
            if (event.getTransparency() != null || event.getSummary().equals("Czas wolny")) {
                markedFreeTimes.add(timestamp);
            } else {
                takenTimes.add(timestamp);
            }
        }
    }

    private static LocalDate getLocalDateFromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static LocalTime getLocalTime(EventDateTime eventDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(eventDateTime.getDateTime().toStringRfc3339().substring(11, 16), formatter);
    }

    public void generateFreeTimestamps(int minutesForLesson, ArrayList<AvailableTime> freeTimes, LocalTime minStartHour, LocalTime maxEndHour) {
        long availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        while (availableMinutesForLesson >= minutesForLesson) {
            LocalTime lessonEndLocalTime = minStartHour.plusMinutes(minutesForLesson);
            freeTimes.add(new AvailableTime(minStartHour, lessonEndLocalTime));
            minStartHour = minStartHour.plusMinutes(MINUTES_TO_ADD);
            availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        }
    }
}
