package com.poznan.put.rest.webservice.restapi.pricing;

public class PricingRequest {
    private String level;
    private String price;
    private int tutorId;

    public PricingRequest(String level, String price, int tutorId) {
        this.level = level;
        this.price = price;
        this.tutorId = tutorId;
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

    public int getTutorId() {
        return tutorId;
    }

    public void setTutorId(int tutorId) {
        this.tutorId = tutorId;
    }
}
