package org.example.dao;

import org.example.model.Course;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void insert(Course course) throws SQLException {
        String sql = "INSERT INTO courses (code, name, credits, max_students, registered_count, lecturer_code) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCode());
            ps.setString(2, course.getName());
            ps.setInt(3, course.getCredits());
            ps.setInt(4, course.getMaxStudents());
            ps.setInt(5, course.getRegisteredCount());
            if (course.getLecturerCode() != null) {
                ps.setString(6, course.getLecturerCode());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    public void update(Course course) throws SQLException {
        String sql = "UPDATE courses SET name = ?, credits = ?, max_students = ?, registered_count = ?, lecturer_code = ? WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getName());
            ps.setInt(2, course.getCredits());
            ps.setInt(3, course.getMaxStudents());
            ps.setInt(4, course.getRegisteredCount());
            if (course.getLecturerCode() != null) {
                ps.setString(5, course.getLecturerCode());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setString(6, course.getCode());
            ps.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM courses WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    public Course findByCode(String code) throws SQLException {
        String sql = "SELECT code, name, credits, max_students, registered_count, lecturer_code FROM courses WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        }
        return null;
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT code, name, credits, max_students, registered_count, lecturer_code FROM courses";
        List<Course> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToCourse(rs));
            }
        }
        return list;
    }

    public List<Course> findByLecturerCode(String lecturerCode) throws SQLException {
        String sql = "SELECT code, name, credits, max_students, registered_count, lecturer_code FROM courses WHERE lecturer_code = ?";
        List<Course> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturerCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCourse(rs));
                }
            }
        }
        return list;
    }

    // For concurrency locking & transaction support
    public Course findByCodeForUpdate(String code, Connection conn) throws SQLException {
        String sql = "SELECT code, name, credits, max_students, registered_count, lecturer_code FROM courses WHERE code = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        }
        return null;
    }

    public void incrementRegisteredCount(String code, Connection conn) throws SQLException {
        String sql = "UPDATE courses SET registered_count = registered_count + 1 WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        return new Course(
            rs.getString("code"),
            rs.getString("name"),
            rs.getInt("credits"),
            rs.getInt("max_students"),
            rs.getInt("registered_count"),
            rs.getString("lecturer_code")
        );
    }
}
