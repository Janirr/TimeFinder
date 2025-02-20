package com.poznan.put.rest.webservice.restapi.controllers.responses;

import java.time.LocalTime;

public record AvailableTimeResponse(LocalTime fromHour, LocalTime untilHour) {
}
