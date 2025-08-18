package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.jpa.PricingRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.Pricing;
import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;

    public PricingService(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    public List<Pricing> findAllByTutor(Tutor tutor) {
        return pricingRepository.findAllByTutor(tutor);
    }

    public void deleteAllByTutor(Tutor tutor) {
        pricingRepository.deleteAllByTutor(tutor);
    }

    public void saveAll(List<Pricing> pricingList) {
        pricingRepository.saveAll(pricingList);
    }
}
