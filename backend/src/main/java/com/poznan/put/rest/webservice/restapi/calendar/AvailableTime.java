package com.poznan.put.rest.webservice.restapi.calendar;

import java.time.LocalTime;

public record AvailableTime(LocalTime fromHour, LocalTime untilHour) {
}
