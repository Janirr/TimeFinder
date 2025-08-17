package com.poznan.put.rest.webservice.restapi.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class User {
    @Size(min = 3, max = 20, message = "Your name has to contain between 3 to 20 characters")
    private String name;
    @Size(min = 2, max = 30, message = "Your surname has to contain between 3 to 20 characters")
    private String surname;
    @Size(min = 9, max = 15, message = "Your phone number has to contain between 9 to 15 characters")
    private String phoneNumber;
    @Email(message = "Email should be valid")
    private String email;
    @JsonIgnore
    private String password;
}
