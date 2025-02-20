package com.poznan.put.rest.webservice.restapi.controllers.requests;

public record RegisterRequest(
        String name,
        String surname,
        String phoneNumber,
        String email,
        String password,
        boolean isTutor) {
}
