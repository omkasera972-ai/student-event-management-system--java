package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.ArrayList;

public class Event {

    private String eventId;
    private String eventName;
    private String venue;

    private int totalSeats;
    private int availableSeats;

    private Date eventDate;
    private java.time.LocalTime startTime;
    private java.time.LocalTime endTime;

    private ArrayList<Student> participants;

    public Event(String eventId, String eventName, String venue,
                 int totalSeats, int availableSeats, Date eventDate,
                 java.time.LocalTime startTime, java.time.LocalTime endTime) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;

        this.participants = new ArrayList<>();
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getVenue() {
        return venue;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public java.time.LocalTime getStartTime() {
        return startTime;
    }

    public java.time.LocalTime getEndTime() {
        return endTime;
    }

    public ArrayList<Student> getParticipants() {
        return participants;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public void setStartTime(java.time.LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(java.time.LocalTime endTime) {
        this.endTime = endTime;
    }

    public void decreaseSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void increaseSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
        }
    }
}