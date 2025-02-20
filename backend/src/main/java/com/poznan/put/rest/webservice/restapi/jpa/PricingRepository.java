package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.model.Pricing;
import com.poznan.put.rest.webservice.restapi.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
    List<Pricing> findAllByTutor(Tutor tutor);

    void deleteAllByTutor(Tutor tutor);
}
