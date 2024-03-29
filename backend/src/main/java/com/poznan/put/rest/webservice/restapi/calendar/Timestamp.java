package com.poznan.put.rest.webservice.restapi.calendar;


import java.time.LocalTime;

public record Timestamp(LocalTime startHour, LocalTime endHour) {
}