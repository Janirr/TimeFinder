package com.poznan.put.rest.webservice.restapi.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long id;
    @Size(min = 3, max = 20, message = "Your name has to contain between 3 to 20 characters")
    private String name;
    @Size(min = 2, max = 30, message = "Your surnname has to contain between 3 to 20 characters")
    private String surname;
    private String typeOfSchool;
    @Max(value = 5, message = "your year of school is really above 5?")
    private int yearOfSchool;
    @Size(min = 3, max = 30, message = "Subject should contain less than 30 characters and more than 2 characters")
    private String subject;
    private boolean advanced;
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Reservation> reservationList;

    public Student() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public Student(Long id, String name, String surname, String typeOfSchool, int yearOfSchool, String subject, boolean advanced, List<Reservation> reservationList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.typeOfSchool = typeOfSchool;
        this.yearOfSchool = yearOfSchool;
        this.subject = subject;
        this.advanced = advanced;
        this.reservationList = reservationList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", typeOfSchool='" + typeOfSchool + '\'' +
                ", yearOfSchool=" + yearOfSchool +
                ", subject='" + subject + '\'' +
                ", advanced=" + advanced +
                ", reservationList=" + reservationList +
                '}';
    }
}
