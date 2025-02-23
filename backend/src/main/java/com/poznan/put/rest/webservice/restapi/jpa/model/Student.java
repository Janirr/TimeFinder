package com.poznan.put.rest.webservice.restapi.jpa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.services.calendar.model.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @JsonIgnore
    private String password;
    @Size(min = 3, max = 20, message = "Your name has to contain between 3 to 20 characters")
    private String name;
    @Size(min = 2, max = 30, message = "Your surname has to contain between 3 to 20 characters")
    private String surname;
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 9, max = 15, message = "Your phone number has to contain between 9 to 15 characters")
    private String phoneNumber;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Reservation> reservationList;

    public Student(int id, String password, String name, String surname, String email, String phoneNumber, List<Reservation> reservationList) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservationList = reservationList;
    }

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public void addReservation(Event event) {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
