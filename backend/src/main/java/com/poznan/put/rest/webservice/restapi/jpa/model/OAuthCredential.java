package com.poznan.put.rest.webservice.restapi.jpa.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OAuthCredential {
    @Id
    private int tutorId;
    private String accessToken;
    private String refreshToken;
    private long expirationTimeMilliseconds;
}
