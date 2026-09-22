package services;
import dao.FacultyDAO;
import java.sql.SQLException;

public class FacultyService {
    private FacultyDAO facultyDAO = new FacultyDAO();

    public String addFaculty(String name, String department, String email) throws SQLException {
        if (!utils.ValidationUtils.isValidEmail(email)) {
            System.out.println("Invalid email address.\nPlease enter a valid email address (ending with @gmail.com or @ssism.org).");
            return null;
        }
        String facultyId = utils.IDGenerator.generateNextId("FACULTY", "F");
        facultyDAO.addFaculty(facultyId, name, department);
        return facultyId;
    }

    public java.util.List<String[]> getAllFaculty() {
        try {
            return facultyDAO.getAllFaculty();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch faculty.");
            return new java.util.ArrayList<>();
        }
    }

    public String[] getFacultyById(String facultyId) {
        try {
            return facultyDAO.getFacultyById(facultyId);
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch faculty details.");
            return null;
        }
    }

    public boolean updateFaculty(String facultyId, String name, String department, String email) throws SQLException {
        if (!utils.ValidationUtils.isValidEmail(email)) {
            System.out.println("Invalid email address.\nPlease enter a valid email address (ending with @gmail.com or @ssism.org).");
            return false;
        }
        int rows = facultyDAO.updateFaculty(facultyId, name, department);
        if (rows == 0) {
            throw new SQLException("Faculty ID Not Found.");
        }
        return true;
    }

    public void deleteFaculty(String facultyId) throws SQLException {
        int rows = facultyDAO.deleteFaculty(facultyId);
        if (rows == 0) {
            throw new SQLException("Faculty ID Not Found.");
        }
    }

    public String[] assignFacultyToEvent(String facultyId, String eventId) throws SQLException, exceptions.EntityNotFoundException, exceptions.DuplicateEntityException {
        return facultyDAO.assignFacultyToEvent(facultyId, eventId);
    }

    public java.util.List<String[]> getEventWiseFaculty() {
        try {
            return facultyDAO.getEventWiseFaculty();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch event-wise faculty.");
            return new java.util.ArrayList<>();
        }
    }
}
