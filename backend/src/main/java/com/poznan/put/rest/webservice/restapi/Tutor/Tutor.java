package com.poznan.put.rest.webservice.restapi.Tutor;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    @OneToMany(mappedBy = "tutor")
    @JsonManagedReference
    private List<Reservation> reservationList;

    public Tutor() {

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tutor(int id, String name, String surname, String phoneNumber, String email, List<Reservation> reservationList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservationList = reservationList;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", reservationList=" + reservationList +
                '}';
    }
}
