package com.poznan.put.rest.webservice.restapi.pricing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import jakarta.persistence.*;


@Entity
public class Pricing {
    @Id
    private String id;
    private String level;
    private String price;

    @ManyToOne(targetEntity = Tutor.class)
    @JoinColumn(name = "tutor_id", foreignKey = @ForeignKey(name = "fk_pricing_tutor"))
    @JsonBackReference
    private Tutor tutor;

    public Pricing(String id, String level, String price, Tutor tutor) {
        this.id = id;
        this.level = level;
        this.price = price;
        this.tutor = tutor;
    }

    public Pricing() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}
