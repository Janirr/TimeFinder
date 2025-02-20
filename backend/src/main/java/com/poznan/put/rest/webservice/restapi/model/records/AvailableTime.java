package com.poznan.put.rest.webservice.restapi.model.records;

import java.time.LocalTime;

public record AvailableTime(LocalTime fromHour, LocalTime untilHour) {
}
