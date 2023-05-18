package com.poznan.put.rest.webservice.restapi.calendar;


import java.time.LocalTime;

public record DayTimestamp(int dayNumber, LocalTime startHour, LocalTime endHour) {

    @Override
    public String toString() {
        return "EventInfo{" +
                "dayNumber=" + dayNumber +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}