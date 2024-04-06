package com.poznan.put.rest.webservice.restapi.reservation;

import java.util.Date;

public class ShortEvent {
    private String summary;
    private Date start;
    private Date end;
    private String attendee;

    public ShortEvent(String summary, Date start, Date end, String attendee) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.attendee = attendee;
    }

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

    public String getAttendee() {
        return attendee;
    }

    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }
}
