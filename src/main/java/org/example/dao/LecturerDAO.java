package org.example.dao;

import org.example.model.Lecturer;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public void insert(Lecturer lecturer) throws SQLException {
        String sql = "INSERT INTO lecturers (code, name, email, dob, department) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturer.getCode());
            ps.setString(2, lecturer.getName());
            ps.setString(3, lecturer.getEmail());
            ps.setDate(4, Date.valueOf(lecturer.getDob()));
            ps.setString(5, lecturer.getDepartment());
            ps.executeUpdate();
        }
    }

    public void update(Lecturer lecturer) throws SQLException {
        String sql = "UPDATE lecturers SET name = ?, email = ?, dob = ?, department = ? WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturer.getName());
            ps.setString(2, lecturer.getEmail());
            ps.setDate(3, Date.valueOf(lecturer.getDob()));
            ps.setString(4, lecturer.getDepartment());
            ps.setString(5, lecturer.getCode());
            ps.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        String sql = "DELETE FROM lecturers WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }

    public Lecturer findByCode(String code) throws SQLException {
        String sql = "SELECT code, name, email, dob, department FROM lecturers WHERE code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLecturer(rs);
                }
            }
        }
        return null;
    }

    public List<Lecturer> findAll() throws SQLException {
        String sql = "SELECT code, name, email, dob, department FROM lecturers";
        List<Lecturer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToLecturer(rs));
            }
        }
        return list;
    }

    private Lecturer mapResultSetToLecturer(ResultSet rs) throws SQLException {
        return new Lecturer(
            rs.getString("code"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getDate("dob").toLocalDate(),
            rs.getString("department")
        );
    }
}
