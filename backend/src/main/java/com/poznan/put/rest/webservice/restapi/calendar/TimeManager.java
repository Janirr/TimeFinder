package com.poznan.put.rest.webservice.restapi.calendar;

import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for managing time in the application.
 * It is used to generate free and busy times for the tutor.
 * TODO: - instead of markedFreeTimes and takenTimes use one ArrayList of Length of Days..
 * TODO: ..and then use DayTimestamp to mark free and busy times inside of it.
 * It will be easier this way to check if there is a free time for the lesson.
 */


public class TimeManager {
    private final ArrayList<DayTimestamp> markedFreeTimes = new ArrayList<>();
    private final ArrayList<DayTimestamp> takenTimes = new ArrayList<>();
    private final ArrayList<ArrayList<AvailableTime>> availableTimes = new ArrayList<>();

    public Date[] getNextDays(int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        Date[] days = new Date[numberOfDays];

        for (int day = 0; day < numberOfDays; day++) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, day);
            days[day] = calendar.getTime();
        }
        return days;
    }

    public ArrayList<ArrayList<AvailableTime>> getFreeTime(int tutorId, String calendarId, int minutesForLesson) throws GeneralSecurityException, IOException {
        long startTime = System.currentTimeMillis();
        CalendarConfig calendarConfig = new CalendarConfig();
        Date[] days = getNextDays(14);
        // Generate free and busy times
        if (calendarConfig.getEventsFromCalendarById(tutorId, calendarId) == null) {
            throw new NullPointerException("Calendar is empty");
        }
        for (Event event : calendarConfig.getEventsFromCalendarById(tutorId, calendarId)) {
            long dateValue = event.getStart().getDateTime().getValue();
            Date date = new Date(dateValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayNumber = calendar.get(Calendar.DATE);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startHour = LocalTime.parse(event.getStart().getDateTime().toStringRfc3339().substring(11, 16), formatter);
            LocalTime endHour = LocalTime.parse(event.getEnd().getDateTime().toStringRfc3339().substring(11, 16), formatter);

            DayTimestamp timestamp = new DayTimestamp(dayNumber, startHour, endHour);

            if (event.getTransparency() == null) {
                takenTimes.add(timestamp);
            } else {
                markedFreeTimes.add(timestamp);
            }
        }
        // Generate available times
        for (Date date : days) {
            ArrayList<AvailableTime> markedFreeTimesInDay = new ArrayList<>();
            LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dayNumber = today.getDayOfMonth();

            for (DayTimestamp freeEventTimeStamp : markedFreeTimes) {
                if (dayNumber == freeEventTimeStamp.dayNumber()) {
                    LocalTime possibleStartHour = freeEventTimeStamp.startHour();
                    LocalTime maximumEndHour = freeEventTimeStamp.endHour();

                    for (DayTimestamp takenEventTimeStamp : takenTimes) {
                        LocalTime takenEventStartTime = takenEventTimeStamp.startHour();
                        if (dayNumber == takenEventTimeStamp.dayNumber()) {
                            if (possibleStartHour != null && takenEventStartTime != null){
                                generateFreeTimestamps(minutesForLesson, date, markedFreeTimesInDay, possibleStartHour, takenEventStartTime);
                                possibleStartHour = takenEventTimeStamp.endHour();
                            }
                        }
                    }

                    if (possibleStartHour != null && maximumEndHour != null){
                        generateFreeTimestamps(minutesForLesson, date, markedFreeTimesInDay, possibleStartHour, maximumEndHour);
                    }
                }
            }
            availableTimes.add(markedFreeTimesInDay);
        }
        long endTime = System.currentTimeMillis();
        long runtime = endTime - startTime;
        System.out.println("Function runtime: " + runtime + " milliseconds");
        return availableTimes;
    }

    private void generateFreeTimestamps(int minutesForLesson, Date date, ArrayList<AvailableTime> freeTimes, LocalTime minStartHour, LocalTime maxEndHour) {
        long availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        while (availableMinutesForLesson >= minutesForLesson) {
            LocalTime lessonEndLocalTime = minStartHour.plusMinutes(minutesForLesson);
            freeTimes.add(new AvailableTime(date, minStartHour, lessonEndLocalTime));
            minStartHour = minStartHour.plusMinutes(15);
            availableMinutesForLesson = ChronoUnit.MINUTES.between(minStartHour, maxEndHour);
        }
    }
}
