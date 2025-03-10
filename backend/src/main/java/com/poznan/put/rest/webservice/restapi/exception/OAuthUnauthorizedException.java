package com.poznan.put.rest.webservice.restapi.exception;

public class OAuthUnauthorizedException extends RuntimeException {
    public OAuthUnauthorizedException(String message) {
        super(message);
    }
}
