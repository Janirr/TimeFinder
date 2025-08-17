package com.poznan.put.rest.webservice.restapi.jpa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
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
}