package com.poznan.put.rest.webservice.restapi.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
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
}
