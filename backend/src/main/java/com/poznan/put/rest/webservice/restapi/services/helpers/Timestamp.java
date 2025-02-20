package com.poznan.put.rest.webservice.restapi.services.helpers;

import java.time.LocalTime;

public record Timestamp(LocalTime startHour, LocalTime endHour) {
}