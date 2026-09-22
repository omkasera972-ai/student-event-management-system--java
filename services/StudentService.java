package services;
import dao.StudentDAO;
import models.Student;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAO();

    public String addStudent(String name, String branch, String email) {
        if (!utils.ValidationUtils.isValidEmail(email)) {
            System.out.println("Invalid email address.\nPlease enter a valid email address (ending with @gmail.com or @ssism.org).");
            return null;
        }
        try {
            String studentId = utils.IDGenerator.generateNextId("STUDENT", "S");
            studentDAO.addStudent(studentId, name, branch, email);
            return studentId;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Duplicate key
                System.out.println("Error: Student ID already exists.");
            } else {
                System.out.println("Database error: Could not add student.");
            }
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    public Student getStudentById(String studentId) {
        try {
            return studentDAO.getStudentById(studentId);
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch student details.");
            return null;
        }
    }

    public List<Student> getAllStudents() {
        try {
            return studentDAO.getAllStudents();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch students.");
            return new ArrayList<>();
        }
    }

    public void showStudents() {
        try {
            studentDAO.showStudents();
        } catch (SQLException e) {
            System.out.println("Database error: Could not fetch students.");
        }
    }

    public boolean updateStudent(String studentId, String name, String branch, String email) {
        if (!utils.ValidationUtils.isValidEmail(email)) {
            System.out.println("Invalid email address.\nPlease enter a valid email address (ending with @gmail.com or @ssism.org).");
            return false;
        }
        try {
            int rows = studentDAO.updateStudent(studentId, name, branch, email);
            if (rows > 0) {
                System.out.println("Student updated successfully.");
                return true;
            } else {
                System.out.println("Error: Student ID Not Found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error: Could not update student.");
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        try {
            int rows = studentDAO.deleteStudent(studentId);
            if (rows > 0) {
                System.out.println("Student deleted successfully.");
                return true;
            } else {
                System.out.println("Error: Student ID Not Found!");
                return false;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                System.out.println("Error: Cannot delete student because they are registered for an event.");
            } else {
                System.out.println("Database error: Could not delete student.");
            }
            return false;
        }
    }
}

