package com.poznan.put.rest.webservice.restapi.student;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
    private String login;
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
    @Size(min = 3, max = 20, message = "Your name has to contain between 3 to 20 characters")
    private String name;
    @Size(min = 2, max = 30, message = "Your surname has to contain between 3 to 20 characters")
    private String surname;
    private String typeOfSchool;
    @Max(value = 5, message = "your year of school is really above 5?")
    private int yearOfSchool;
    private boolean advanced;
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Reservation> reservationList;

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTypeOfSchool() {
        return typeOfSchool;
    }

    public void setTypeOfSchool(String typeOfSchool) {
        this.typeOfSchool = typeOfSchool;
    }

    public int getYearOfSchool() {
        return yearOfSchool;
    }

    public void setYearOfSchool(int yearOfSchool) {
        this.yearOfSchool = yearOfSchool;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public Student(int id, String login, String password, String name, String surname, String typeOfSchool, int yearOfSchool, boolean advanced, List<Reservation> reservationList) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.typeOfSchool = typeOfSchool;
        this.yearOfSchool = yearOfSchool;
        this.advanced = advanced;
        this.reservationList = reservationList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", typeOfSchool='" + typeOfSchool + '\'' +
                ", yearOfSchool=" + yearOfSchool +
                ", advanced=" + advanced +
                ", reservationList=" + reservationList +
                '}';
    }
}
