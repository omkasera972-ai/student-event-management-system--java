package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class EventDAO {

    public void addEvent(String eventId, String eventName, String venue, int totalSeats, int availableSeats, String eventDate, java.time.LocalTime startTime, java.time.LocalTime endTime) throws SQLException {
        String sql = "INSERT INTO events (event_id, event_name, venue, total_seats, available_seats, event_date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventId);
            ps.setString(2, eventName);
            ps.setString(3, venue);
            ps.setInt(4, totalSeats);
            ps.setInt(5, availableSeats);
            ps.setDate(6, Date.valueOf(eventDate));
            ps.setTime(7, java.sql.Time.valueOf(startTime));
            ps.setTime(8, java.sql.Time.valueOf(endTime));
            ps.executeUpdate();
        }
    }

    public List<String[]> getAvailableEventsForRegistration() throws SQLException {
        List<String[]> events = new ArrayList<>();
        String sql = "SELECT event_id, event_name, venue, available_seats, event_date, start_time, end_time FROM events";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
            while (rs.next()) {
                String dateStr = rs.getDate("event_date") != null ? rs.getDate("event_date").toString() : "N/A";
                String[] parts = dateStr.split("-");
                if (parts.length == 3) {
                    dateStr = parts[2] + "-" + parts[1] + "-" + parts[0];
                }
                
                java.sql.Time st = rs.getTime("start_time");
                java.sql.Time et = rs.getTime("end_time");
                String timeStr = "N/A";
                if (st != null && et != null) {
                    timeStr = st.toLocalTime().format(timeFormatter) + " - " + et.toLocalTime().format(timeFormatter);
                }
                events.add(new String[]{
                    rs.getString("event_id"),
                    rs.getString("event_name"),
                    rs.getString("venue"),
                    String.valueOf(rs.getInt("available_seats")),
                    dateStr,
                    timeStr
                });
            }
        }
        return events;
    }

    public void showEvents() throws SQLException {
        String sql = "SELECT * FROM events";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\n==========================================================================================================================");
            System.out.println("                                      ALL EVENTS");
            System.out.println("==========================================================================================================================");
            System.out.println();
            System.out.printf("%-15s %-28s %-15s %-14s %-18s %-14s %-25s%n",
                    "Event ID",
                    "Event Name",
                    "Venue",
                    "Total Seats",
                    "Available Seats",
                    "Date",
                    "Time");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            
            int count = 0;
            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
            while (rs.next()) {
                String dateStr = rs.getDate("event_date") != null ? rs.getDate("event_date").toString() : "N/A";
                String[] parts = dateStr.split("-");
                String formattedDate = dateStr;
                if (parts.length == 3) {
                    formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                }
                
                java.sql.Time st = rs.getTime("start_time");
                java.sql.Time et = rs.getTime("end_time");
                String timeStr = "N/A";
                if (st != null && et != null) {
                    timeStr = st.toLocalTime().format(timeFormatter) + " - " + et.toLocalTime().format(timeFormatter);
                }
                
                System.out.printf("%-15s %-28s %-15s %-14d %-18d %-14s %-25s%n",
                        rs.getString("event_id"),
                        rs.getString("event_name"),
                        rs.getString("venue"),
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        formattedDate,
                        timeStr);
                count++;
            }
            
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.println("Total Events : " + count);
            System.out.println();
            System.out.println("==========================================================================================================================");
        }
    }

    public int updateEvent(String eventId, String eventName, String venue, int totalSeats, int availableSeats, String eventDate, java.time.LocalTime startTime, java.time.LocalTime endTime) throws SQLException {
        String sql = "UPDATE events SET event_name = ?, venue = ?, total_seats = ?, available_seats = ?, event_date = ?, start_time = ?, end_time = ? WHERE event_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventName);
            ps.setString(2, venue);
            ps.setInt(3, totalSeats);
            ps.setInt(4, availableSeats);
            ps.setDate(5, Date.valueOf(eventDate));
            ps.setTime(6, java.sql.Time.valueOf(startTime));
            ps.setTime(7, java.sql.Time.valueOf(endTime));
            ps.setString(8, eventId);
            return ps.executeUpdate();
        }
    }

    public models.Event getEventById(String eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new models.Event(
                        rs.getString("event_id"),
                        rs.getString("event_name"),
                        rs.getString("venue"),
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        rs.getDate("event_date"),
                        rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null,
                        rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null
                    );
                }
            }
        }
        return null;
    }

    public int deleteEvent(String eventId) throws SQLException {
        String deleteRegistrationsSQL = "DELETE FROM registrations WHERE event_id = ?";
        String deleteEventFacultySQL = "DELETE FROM event_faculty WHERE event_id = ?";
        String deleteEventSQL = "DELETE FROM events WHERE event_id = ?";
        
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            
            try (PreparedStatement psReg = con.prepareStatement(deleteRegistrationsSQL);
                 PreparedStatement psFac = con.prepareStatement(deleteEventFacultySQL);
                 PreparedStatement psEvt = con.prepareStatement(deleteEventSQL)) {
                 
                psReg.setString(1, eventId);
                psReg.executeUpdate();
                
                psFac.setString(1, eventId);
                psFac.executeUpdate();
                
                psEvt.setString(1, eventId);
                int rows = psEvt.executeUpdate();
                
                con.commit();
                return rows;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
}
