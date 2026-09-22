package services;
import dao.RegistrationDAO;
import exceptions.EntityNotFoundException;
import exceptions.NoSeatsAvailableException;
import exceptions.DuplicateEntityException;
import java.sql.SQLException;

public class RegistrationService {
    private RegistrationDAO registrationDAO = new RegistrationDAO();

    public String[] addRegistration(String studentId, String eventId) throws SQLException, EntityNotFoundException, NoSeatsAvailableException, DuplicateEntityException {
        String regId = utils.IDGenerator.generateNextId("REGISTRATION", "R");
        String[] result = registrationDAO.registerStudentForEvent(regId, studentId, eventId);
        // add regId to the beginning or end of result, or print it.
        // Wait, the return type is String[]. Let's just return a new array with regId included.
        String[] newResult = new String[result.length + 1];
        newResult[0] = regId;
        System.arraycopy(result, 0, newResult, 1, result.length);
        return newResult;
    }

        public java.util.List<RegistrationDAO.EventRegistrationGroup> getGroupedRegistrations() {
        try {
            return registrationDAO.getGroupedRegistrations();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch grouped registrations.");
            return new java.util.ArrayList<>();
        }
    }

    public java.util.List<RegistrationDAO.RegistrationDetail> getAllRegistrationsFlat() {
        try {
            return registrationDAO.getAllRegistrationsFlat();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch registrations.");
            return new java.util.ArrayList<>();
        }
    }

    public RegistrationDAO.RegistrationDetail getRegistrationDetails(String regId) {
        try {
            return registrationDAO.getRegistrationDetails(regId);
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch registration details.");
            return null;
        }
    }

    public String[] updateRegistration(String regId, String newEventId) throws SQLException, EntityNotFoundException, NoSeatsAvailableException, DuplicateEntityException {
        return registrationDAO.updateRegistration(regId, newEventId);
    }

    public void deleteRegistration(String regId) throws SQLException, EntityNotFoundException {
        registrationDAO.deleteRegistration(regId);
    }
}
