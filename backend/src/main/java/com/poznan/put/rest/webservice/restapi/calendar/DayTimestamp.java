package com.poznan.put.rest.webservice.restapi.calendar;


import java.time.LocalDate;
import java.time.LocalTime;

public record DayTimestamp(LocalDate date, LocalTime startHour, LocalTime endHour) {

@Override
    public String toString() {
        return "DayTimestamp{" +
                "date=" + date +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}