package services;
import dao.EventDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class EventService {
    private EventDAO eventDAO = new EventDAO();

    private java.time.LocalTime parseTime(String timeStr) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.US);
            return java.time.LocalTime.parse(timeStr.toUpperCase(), formatter);
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }

    public String addEvent(String eventName, String venue, int totalSeats, int availableSeats, String eventDate, String startTimeStr, String endTimeStr) {
        java.time.LocalTime st = parseTime(startTimeStr);
        java.time.LocalTime et = parseTime(endTimeStr);
        
        if (st == null || et == null) {
            System.out.println("Error: Invalid time format. Please use HH:MM AM/PM.");
            return null;
        }
        if (!st.isBefore(et)) {
            System.out.println("Error: Start Time must be before End Time.");
            return null;
        }

        try {
            String eventId = utils.IDGenerator.generateNextId("EVENT", "E");
            eventDAO.addEvent(eventId, eventName, venue, totalSeats, availableSeats, eventDate, st, et);
            System.out.println("Event Added Successfully!");
            return eventId;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Error: Event ID already exists.");
            } else {
                System.out.println("Database error: Could not add event. (Check date format YYYY-MM-DD)");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
        }
        return null;
    }

    public List<String[]> getAvailableEventsForRegistration() {
        try {
            return eventDAO.getAvailableEventsForRegistration();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public void showEvents() {
        try {
            eventDAO.showEvents();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch events.");
        }
    }

    public void updateEvent(String eventId, String eventName, String venue, int totalSeats, int availableSeats, String eventDate, String startTimeStr, String endTimeStr) {
        java.time.LocalTime st = parseTime(startTimeStr);
        java.time.LocalTime et = parseTime(endTimeStr);
        
        if (st == null || et == null) {
            System.out.println("Error: Invalid time format. Please use HH:MM AM/PM.");
            return;
        }
        if (!st.isBefore(et)) {
            System.out.println("Error: Start Time must be before End Time.");
            return;
        }

        try {
            int rows = eventDAO.updateEvent(eventId, eventName, venue, totalSeats, availableSeats, eventDate, st, et);
            if (rows > 0) System.out.println("Event Updated Successfully!");
            else System.out.println("Error: Event ID Not Found!");
        } catch (SQLException e) {
            System.out.println("Database error: Could not update event.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public models.Event getEventById(String eventId) {
        try {
            return eventDAO.getEventById(eventId);
        } catch (SQLException e) {
            return null;
        }
    }

    public void deleteEvent(String eventId, String eventName) {
        try {
            int rows = eventDAO.deleteEvent(eventId);
            if (rows > 0) {
                System.out.println("\n============================================================");
                System.out.println("                EVENT DELETED SUCCESSFULLY");
                System.out.println("============================================================");
                System.out.println("Event ID   : " + eventId);
                System.out.println("Event Name : " + eventName);
                System.out.println("Status     : Event and related registrations deleted");
                System.out.println("============================================================");
            } else {
                System.out.println("Error: Event ID Not Found!");
            }
        } catch (SQLException e) {
            System.out.println("Database error: Could not delete event.");
        }
    }
}
