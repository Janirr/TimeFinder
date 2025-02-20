package com.poznan.put.rest.webservice.restapi.model.records;

public record RegisterRequest(
        String name,
        String surname,
        String phoneNumber,
        String email,
        String password,
        boolean isTutor) {
}
