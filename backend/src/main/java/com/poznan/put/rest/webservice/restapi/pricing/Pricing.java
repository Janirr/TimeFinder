package com.poznan.put.rest.webservice.restapi.pricing;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import jakarta.persistence.*;


@Entity
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String level;
    private String price;

    @ManyToOne(targetEntity = Tutor.class)
    @JoinColumn(name = "tutor_id", foreignKey = @ForeignKey(name = "fk_pricing_tutor"))
    private Tutor tutor;

    public Pricing(Long id, String level, String price, Tutor tutor) {
        this.id = id;
        this.level = level;
        this.price = price;
        this.tutor = tutor;
    }

    public Pricing() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
