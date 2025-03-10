package com.poznan.put.rest.webservice.restapi.jpa.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OAuthCredential {

    @Id
    private int tutorId;
    private String accessToken;
    private String refreshToken;
    private long expirationTimeMilliseconds;

    // Getters and setters

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpirationTimeMilliseconds() {
        return expirationTimeMilliseconds;
    }

    public void setExpirationTimeMilliseconds(long expirationTimeMilliseconds) {
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
    }
}
