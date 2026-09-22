package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {

    public Map<String, Integer> getOverallSummary() throws SQLException {
        Map<String, Integer> summary = new HashMap<>();
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM students");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Students", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM events");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Events", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM registrations");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Registrations", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM faculty");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Faculty", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(DISTINCT faculty_id) FROM event_faculty");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Assigned Faculty", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM events e LEFT JOIN event_faculty ef ON e.event_id = ef.event_id WHERE ef.event_id IS NULL");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Unassigned Events", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT SUM(available_seats) FROM events");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Available Seats", rs.getInt(1));
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT SUM(total_seats - available_seats) FROM events");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) summary.put("Total Occupied Seats", rs.getInt(1));
            }
        }
        return summary;
    }

    public List<String[]> getStudentReport() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT student_id, name, branch, email FROM students";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("student_id"), rs.getString("name"), rs.getString("branch"), rs.getString("email")});
            }
        }
        return list;
    }

    public List<String[]> getEventReport() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT event_id, event_name, venue, total_seats, available_seats, event_date, start_time, end_time FROM events";
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

                list.add(new String[]{rs.getString("event_id"), rs.getString("event_name"), rs.getString("venue"), 
                                      String.valueOf(rs.getInt("total_seats")), String.valueOf(rs.getInt("available_seats")),
                                      dateStr, timeStr});
            }
        }
        return list;
    }

    public List<String[]> getRegistrationReport() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT r.reg_id, s.student_id, s.name, e.event_id, e.event_name " +
                     "FROM registrations r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "JOIN events e ON r.event_id = e.event_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("reg_id"), rs.getString("student_id"), 
                                      rs.getString("name"), rs.getString("event_id"), rs.getString("event_name")});
            }
        }
        return list;
    }

    public List<String[]> getSeatAvailabilityReport() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT event_id, event_name, total_seats, (total_seats - available_seats) as booked, available_seats FROM events";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("event_id"), rs.getString("event_name"), 
                                      String.valueOf(rs.getInt("total_seats")), String.valueOf(rs.getInt("booked")), 
                                      String.valueOf(rs.getInt("available_seats"))});
            }
        }
        return list;
    }

    public List<String[]> getFacultyAssignmentReport() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, f.faculty_id, f.name " +
                     "FROM events e " +
                     "LEFT JOIN event_faculty ef ON e.event_id = ef.event_id " +
                     "LEFT JOIN faculty f ON ef.faculty_id = f.faculty_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String fid = rs.getString("faculty_id");
                String fname = rs.getString("name");
                if (fid == null) {
                    fid = "--";
                    fname = "Not Assigned";
                }
                list.add(new String[]{rs.getString("event_id"), rs.getString("event_name"), fid, fname});
            }
        }
        return list;
    }

    public List<String[]> getMostRegisteredEvents() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sqlMax = "SELECT MAX(total_seats - available_seats) FROM events";
        int maxBooked = 0;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlMax);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                maxBooked = rs.getInt(1);
            }
        }
        
        if (maxBooked > 0) {
            String sql = "SELECT event_id, event_name, venue, total_seats, (total_seats - available_seats) as booked, available_seats " +
                         "FROM events WHERE (total_seats - available_seats) = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maxBooked);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new String[]{rs.getString("event_id"), rs.getString("event_name"), rs.getString("venue"), 
                                              String.valueOf(rs.getInt("total_seats")), String.valueOf(rs.getInt("booked")), 
                                              String.valueOf(rs.getInt("available_seats"))});
                    }
                }
            }
        }
        return list;
    }
}
