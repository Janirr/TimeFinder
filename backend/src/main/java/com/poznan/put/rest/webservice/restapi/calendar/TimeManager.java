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

public class TimeManager {
    private final ArrayList<DayTimestamp> freeTimes = new ArrayList<>();
    private final ArrayList<DayTimestamp> busyTimes = new ArrayList<>();
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
        CalendarConfig calendarConfig = new CalendarConfig();
        Date[] days = getNextDays(14);
        // Generate free and busy times
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
                busyTimes.add(timestamp);
            } else {
                freeTimes.add(timestamp);
            }
        }
        // Generate available times
        for (Date date : days) {
            ArrayList<AvailableTime> freeTimesInDay = new ArrayList<>();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dayNumber = localDate.getDayOfMonth();

            for (DayTimestamp freeEvent : freeTimes) {
                if (dayNumber == freeEvent.dayNumber()) {
                    LocalTime freeStartTime = freeEvent.startHour();
                    LocalTime freeEndTime = freeEvent.endHour();

                    for (DayTimestamp busyEvent : busyTimes) {
                        if (dayNumber == busyEvent.dayNumber()) {
                            LocalTime busyStartTime = busyEvent.startHour();
                            if (freeStartTime != null && busyStartTime != null) {
                                long availableMinutes = ChronoUnit.MINUTES.between(freeStartTime, busyStartTime);
                                while (availableMinutes >= minutesForLesson) {
                                    LocalTime lessonEnd = freeStartTime.plusMinutes(minutesForLesson);
                                    freeTimesInDay.add(new AvailableTime(date, freeStartTime, lessonEnd));
                                    freeStartTime = freeStartTime.plusMinutes(10);
                                    availableMinutes = ChronoUnit.MINUTES.between(freeStartTime, busyStartTime);
                                }
                                freeStartTime = busyEvent.endHour();
                            }
                        }
                    }
                    freeTimesInDay.add(new AvailableTime(date, freeStartTime, freeEndTime));
                }
            }

            availableTimes.add(freeTimesInDay);
        }

        return availableTimes;
    }
}
