package com.poznan.put.rest.webservice.restapi.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.student.Student;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat
    private LocalDate day;
    private LocalTime startHour;
    private LocalTime endHour;
    private String location;
    private String subject;
    private String status;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_reservation_student"))
    @JsonBackReference
    private Student student;

    @ManyToOne(targetEntity = Tutor.class)
    @JoinColumn(name = "tutor_id", foreignKey = @ForeignKey(name = "fk_reservation_tutor"))
    @JsonBackReference
    private Tutor tutor;

    public Reservation() {

    }

    public Reservation(Long id, LocalDate day, LocalTime startHour, LocalTime endHour, String location, String subject, String status, Student student, Tutor tutor) {
        this.id = id;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.location = location;
        this.subject = subject;
        this.status = status;
        this.student = student;
        this.tutor = tutor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", day=" + day +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", location='" + location + '\'' +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                ", student=" + student +
                ", tutor=" + tutor +
                '}';
    }
}