package com.poznan.put.rest.webservice.restapi.jpa;

import java.util.Date;

public class ShortEvent {
    private String summary;
    private Date start;
    private Date end;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
}
