package org.example.dao;

import org.example.model.Student;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void insert(Student student) throws SQLException {
        String sql = "INSERT INTO students (code, name, email, dob, major, gpa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getCode());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setDate(4, Date.valueOf(student.getDob()));
            ps.setString(5, student.getMajor());
            ps.setDouble(6, student.getGpa());
            ps.executeUpdate();
        }
    }

    public void update(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, email = ?, dob = ?, major = ?, gpa = ? WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setDate(3, Date.valueOf(student.getDob()));
            ps.setString(4, student.getMajor());
            ps.setDouble(5, student.getGpa());
            ps.setString(6, student.getCode());
            ps.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM students WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    public Student findByCode(String code) throws SQLException {
        String sql = "SELECT code, name, email, dob, major, gpa FROM students WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT code, name, email, dob, major, gpa FROM students";
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        }
        return list;
    }

    public List<Student> findByCourseCode(String courseCode) throws SQLException {
        String sql = "SELECT s.code, s.name, s.email, s.dob, s.major, s.gpa " +
                     "FROM students s " +
                     "JOIN enrollments e ON s.code = e.student_code " +
                     "WHERE e.course_code = ? " +
                     "ORDER BY s.code";
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToStudent(rs));
                }
            }
        }
        return list;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("code"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getDate("dob").toLocalDate(),
            rs.getString("major"),
            rs.getDouble("gpa")
        );
    }
}
