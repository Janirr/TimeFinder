package com.poznan.put.rest.webservice.restapi.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.api.services.calendar.model.EventDateTime;
import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.student.Student;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date start;
    private Date end;
    private String summary;

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

    public Reservation(int id, Date start, Date end, String summary, Student student, Tutor tutor) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.student = student;
        this.tutor = tutor;
    }

    public Reservation(Date start, Date end, String summary, Student student, Tutor tutor) {
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.student = student;
        this.tutor = tutor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
}