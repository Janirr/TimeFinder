package com.poznan.put.rest.webservice.restapi.jpa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Reservation {
    @Id
    private String id;
    private Date start;
    private Date end;
    private String summary;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_reservation_student"))
    @JsonManagedReference
    private Student student;

    @ManyToOne(targetEntity = Tutor.class)
    @JoinColumn(name = "tutor_id", foreignKey = @ForeignKey(name = "fk_reservation_tutor"))
    @JsonBackReference
    private Tutor tutor;

    public Reservation() {

    }

    public Reservation(String id, Date start, Date end, String summary, Student student, Tutor tutor) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.summary = summary;
        this.student = student;
        this.tutor = tutor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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