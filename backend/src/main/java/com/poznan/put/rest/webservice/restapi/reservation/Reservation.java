package com.poznan.put.rest.webservice.restapi.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.student.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
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
    @Future
    private LocalTime startHour;
    @Future
    private LocalTime endHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",foreignKey = @ForeignKey(name = "fk_reservation_student"))
    @JsonBackReference
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",foreignKey = @ForeignKey(name = "fk_reservation_tutor"))
    @JsonBackReference
    private Tutor tutor;

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    /*public void setId(Long id) {
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

    public Student getStudent() {
        return student;
    }
    */
    public void setStudent(Student student) {
        this.student = student;
    }
    /*
    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
    */
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", day=" + day +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", student=" + student +
                ", tutor=" + tutor +
                '}';
    }
}
