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
import java.util.*;

public class TimeManagerUtil {
    public static final int NUMBER_OF_DAYS = 14;
    public static final int MINUTES_TO_ADD = 15;
    private final CalendarConfig calendarConfig = new CalendarConfig();
    private HashMap<LocalDate, List<Timestamp>> markedFreeTimes = new HashMap<>();
    private HashMap<LocalDate, List<Timestamp>> takenTimes = new HashMap<>();

    public static LocalDate getLocalDateFromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static LocalTime getLocalTime(EventDateTime eventDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(eventDateTime.getDateTime().toStringRfc3339().substring(11, 16), formatter);
    }

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

    public HashMap<LocalDate, List<AvailableTime>> getFreeTime(int tutorId, String calendarId, int minutesForLesson) {
        HashMap<LocalDate, List<AvailableTime>> availableTime = new HashMap<>();
        try {
            Date[] days = getNextDays(NUMBER_OF_DAYS);
            getCalendarEvents(tutorId, calendarId);
            generateAvailableTimes(minutesForLesson, availableTime, days);
        } catch (Exception e) {
            System.out.println("Error in getting free time: " + e.getMessage());
        }
        return availableTime;
    }

    public void generateAvailableTimes(int minutesForLesson, HashMap<LocalDate, List<AvailableTime>> availableTime, Date[] days) {
        for (Date date : days) {
            List<AvailableTime> markedFreeTimesInDay = new ArrayList<>();
            LocalDate day = getLocalDateFromDate(date);

            if (!markedFreeTimes.containsKey(day)) {
                availableTime.put(day, markedFreeTimesInDay);
                continue;
            }

            for (Timestamp freeEventTimeStamp : markedFreeTimes.get(day)) {
                LocalTime possibleStartHour = freeEventTimeStamp.startHour();
                LocalTime maximumEndHour = freeEventTimeStamp.endHour();

                if (takenTimes.containsKey(day)) {
                    for (Timestamp takenEventTimeStamp : takenTimes.get(day)) {
                        LocalTime takenEventStartTime = takenEventTimeStamp.startHour();
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

            availableTime.put(day, markedFreeTimesInDay);
        }
    }

    public void getCalendarEvents(int tutorId, String calendarId) throws GeneralSecurityException, IOException {
        for (Event event : calendarConfig.getEventsFromCalendarById(tutorId, calendarId)) {
            LocalDate eventDay = getLocalDateFromDate(new Date(event.getStart().getDateTime().getValue()));
            LocalTime startHour = getLocalTime(event.getStart());
            LocalTime endHour = getLocalTime(event.getEnd());
            Timestamp timestamp = new Timestamp(startHour, endHour);
            if (event.getTransparency() != null || event.getSummary().equals("Czas wolny")) {
                if (markedFreeTimes.containsKey(eventDay)) {
                    markedFreeTimes.get(eventDay).add(timestamp);
                } else {
                    markedFreeTimes.put(eventDay, List.of(timestamp));
                }
            } else {
                if (takenTimes.containsKey(eventDay)) {
                    takenTimes.get(eventDay).add(timestamp);
                } else {
                    takenTimes.put(eventDay, List.of(timestamp));
                }
            }
        }
    }

    public void generateFreeTimestamps(int minutesForLesson, List<AvailableTime> freeTimes, LocalTime minStartHour, LocalTime maxEndHour) {
        long availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        while (availableMinutesForLesson >= minutesForLesson) {
            LocalTime lessonEndLocalTime = minStartHour.plusMinutes(minutesForLesson);
            freeTimes.add(new AvailableTime(minStartHour, lessonEndLocalTime));
            minStartHour = minStartHour.plusMinutes(MINUTES_TO_ADD);
            availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        }
    }

    public void setMarkedFreeTimes(HashMap<LocalDate, List<Timestamp>> markedFreeTimes) {
        this.markedFreeTimes = markedFreeTimes;
    }

    public void setTakenTimes(HashMap<LocalDate, List<Timestamp>> takenTimes) {
        this.takenTimes = takenTimes;
    }
}
