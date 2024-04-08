package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.pricing.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
    List<Pricing> findAllByTutor(Tutor tutor);

    void deleteAllByTutor(Tutor tutor);
}
