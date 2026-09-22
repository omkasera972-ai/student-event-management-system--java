package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyDAO {
    public void addFaculty(String facultyId, String name, String department) throws SQLException {
        String sql = "INSERT INTO faculty (faculty_id, name, department) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, facultyId);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.executeUpdate();
        }
    }

    public java.util.List<String[]> getAllFaculty() throws SQLException {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM faculty";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("faculty_id"), rs.getString("name"), rs.getString("department")});
            }
        }
        return list;
    }

    public String[] getFacultyById(String facultyId) throws SQLException {
        String sql = "SELECT * FROM faculty WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("faculty_id"), rs.getString("name"), rs.getString("department")};
                }
            }
        }
        return null;
    }

    public int updateFaculty(String facultyId, String name, String department) throws SQLException {
        String sql = "UPDATE faculty SET name = ?, department = ? WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, facultyId);
            return ps.executeUpdate();
        }
    }

    public int deleteFaculty(String facultyId) throws SQLException {
        String sql = "DELETE FROM faculty WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, facultyId);
            return ps.executeUpdate();
        }
    }

    public String[] assignFacultyToEvent(String facultyId, String eventId) throws SQLException, exceptions.EntityNotFoundException, exceptions.DuplicateEntityException {
        String eventName = null;
        String checkEvent = "SELECT event_name FROM events WHERE event_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(checkEvent)) {
            ps.setString(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new exceptions.EntityNotFoundException("Event ID not found.");
                eventName = rs.getString("event_name");
            }
        }
        
        String facName = null, dept = null;
        String checkFac = "SELECT name, department FROM faculty WHERE faculty_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(checkFac)) {
            ps.setString(1, facultyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new exceptions.EntityNotFoundException("Faculty ID not found.");
                facName = rs.getString("name");
                dept = rs.getString("department");
            }
        }
        
        String sql = "INSERT INTO event_faculty (event_id, faculty_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eventId);
            ps.setString(2, facultyId);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { 
                throw new exceptions.DuplicateEntityException("Event already has an assigned faculty.");
            }
            throw e;
        }
        return new String[]{eventName, facName, dept};
    }

    public java.util.List<String[]> getEventWiseFaculty() throws SQLException {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, f.faculty_id, f.name " +
                     "FROM event_faculty ef " +
                     "JOIN events e ON ef.event_id = e.event_id " +
                     "JOIN faculty f ON ef.faculty_id = f.faculty_id " +
                     "ORDER BY e.event_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("event_id"), 
                    rs.getString("event_name"), 
                    rs.getString("faculty_id"), 
                    rs.getString("name")
                });
            }
        }
        return list;
    }
}
