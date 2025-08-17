package com.poznan.put.rest.webservice.restapi.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Tutor extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    private String password;
    private String calendarId;
    private String subject;
    @OneToMany(mappedBy = "tutor")
    @JsonManagedReference
    private List<Reservation> reservationList;
}
