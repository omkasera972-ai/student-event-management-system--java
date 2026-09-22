package services;

import dao.ReportDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReportService {

    private ReportDAO reportDAO = new ReportDAO();

    public Map<String, Integer> getOverallSummary() {
        try {
            return reportDAO.getOverallSummary();
        } catch (SQLException e) {
            System.out.println("Database error generating Overall Summary: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getStudentReport() {
        try {
            return reportDAO.getStudentReport();
        } catch (SQLException e) {
            System.out.println("Database error generating Student Report: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getEventReport() {
        try {
            return reportDAO.getEventReport();
        } catch (SQLException e) {
            System.out.println("Database error generating Event Report: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getRegistrationReport() {
        try {
            return reportDAO.getRegistrationReport();
        } catch (SQLException e) {
            System.out.println("Database error generating Registration Report: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getSeatAvailabilityReport() {
        try {
            return reportDAO.getSeatAvailabilityReport();
        } catch (SQLException e) {
            System.out.println("Database error generating Seat Availability Report: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getFacultyAssignmentReport() {
        try {
            return reportDAO.getFacultyAssignmentReport();
        } catch (SQLException e) {
            System.out.println("Database error generating Faculty Assignment Report: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getMostRegisteredEvents() {
        try {
            return reportDAO.getMostRegisteredEvents();
        } catch (SQLException e) {
            System.out.println("Database error generating Most Registered Event Report: " + e.getMessage());
            return null;
        }
    }
}
