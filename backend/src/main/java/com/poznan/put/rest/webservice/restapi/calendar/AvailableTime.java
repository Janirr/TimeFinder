package com.poznan.put.rest.webservice.restapi.calendar;

import java.time.LocalTime;
import java.util.Date;

public record AvailableTime(Date date, LocalTime fromHour, LocalTime untilHour) {

    @Override
    public String toString() {
        return "AvailableTime{" +
                "date=" + date +
                ", fromHour=" + fromHour +
                ", untilHour=" + untilHour +
                '}';
    }
}
