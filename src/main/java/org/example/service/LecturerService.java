package org.example.service;

import org.example.dao.LecturerDAO;
import org.example.model.Lecturer;

import java.sql.SQLException;
import java.util.List;

public class LecturerService {
    private final LecturerDAO lecturerDAO;

    public LecturerService() {
        this.lecturerDAO = new LecturerDAO();
    }

    public void addLecturer(Lecturer lecturer) throws SQLException {
        if (lecturer.getCode() == null || lecturer.getCode().isBlank()) {
            throw new IllegalArgumentException("Lecturer code cannot be empty.");
        }
        if (lecturer.getName() == null || lecturer.getName().isBlank()) {
            throw new IllegalArgumentException("Lecturer name cannot be empty.");
        }
        if (lecturer.getEmail() == null || lecturer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Lecturer email cannot be empty.");
        }
        lecturerDAO.insert(lecturer);
    }

    public void updateLecturer(Lecturer lecturer) throws SQLException {
        if (lecturer.getCode() == null || lecturer.getCode().isBlank()) {
            throw new IllegalArgumentException("Lecturer code is required for update.");
        }
        lecturerDAO.update(lecturer);
    }

    public void deleteLecturer(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Lecturer code is required for deletion.");
        }
        lecturerDAO.delete(code);
    }

    public Lecturer getLecturerByCode(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Lecturer code is required.");
        }
        return lecturerDAO.findByCode(code);
    }

    public List<Lecturer> getAllLecturers() throws SQLException {
        return lecturerDAO.findAll();
    }
}
