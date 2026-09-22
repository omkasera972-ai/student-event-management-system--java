package dao;

import models.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void addStudent(String studentId, String name, String branch, String email) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, branch, email) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, name);
            ps.setString(3, branch);
            ps.setString(4, email);
            ps.executeUpdate();
        }
    }

    public Student getStudentById(String studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getString("name"),
                        rs.getString("student_id"),
                        rs.getString("email"),
                        rs.getString("branch")
                    );
                }
            }
        }
        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(
                    rs.getString("name"),
                    rs.getString("student_id"),
                    rs.getString("email"),
                    rs.getString("branch")
                ));
            }
        }
        return list;
    }

    public void showStudents() throws SQLException {
        List<Student> list = getAllStudents();
        System.out.println("\n============================================================");
        System.out.println("                    ALL STUDENTS");
        System.out.println("============================================================");
        System.out.println();
        System.out.printf("%-15s %-18s %-12s %s\n", "Student ID", "Name", "Branch", "Email");
        System.out.println("------------------------------------------------------------");
        for (Student st : list) {
            System.out.printf("%-15s %-18s %-12s %s\n", st.getId(), st.getName(), st.getBranch(), st.getEmail());
        }
        System.out.println();
        System.out.println("============================================================");
        System.out.println("Total Students : " + list.size());
        System.out.println("============================================================");
    }

    public int updateStudent(String studentId, String name, String branch, String email) throws SQLException {
        String sql = "UPDATE students SET name = ?, branch = ?, email = ? WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, branch);
            ps.setString(3, email);
            ps.setString(4, studentId);
            return ps.executeUpdate();
        }
    }

    public int deleteStudent(String studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            return ps.executeUpdate();
        }
    }
}

