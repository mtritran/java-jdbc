package org.example.dao;

import org.example.model.Enrollment;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public void insert(Enrollment enrollment, Connection conn) throws SQLException {
        String sql = "INSERT INTO enrollments (student_code, course_code, enrollment_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, enrollment.getStudentCode());
            ps.setString(2, enrollment.getCourseCode());
            ps.setTimestamp(3, Timestamp.valueOf(enrollment.getEnrollmentDate()));
            ps.executeUpdate();
        }
    }

    public boolean isEnrolled(String studentCode, String courseCode, Connection conn) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_code = ? AND course_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            ps.setString(2, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void delete(String studentCode, String courseCode) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_code = ? AND course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentCode);
            ps.setString(2, courseCode);
            ps.executeUpdate();
        }
    }

    public List<Enrollment> findAll() throws SQLException {
        String sql = "SELECT student_code, course_code, enrollment_date FROM enrollments";
        List<Enrollment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getString("student_code"),
                    rs.getString("course_code"),
                    rs.getTimestamp("enrollment_date").toLocalDateTime()
                ));
            }
        }
        return list;
    }
}
