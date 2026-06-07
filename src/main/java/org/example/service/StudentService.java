package org.example.service;

import org.example.dao.StudentDAO;
import org.example.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public void addStudent(Student student) throws SQLException {
        if (student.getCode() == null || student.getCode().isBlank()) {
            throw new IllegalArgumentException("Student code cannot be empty.");
        }
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new IllegalArgumentException("Student email cannot be empty.");
        }
        studentDAO.insert(student);
    }

    public void updateStudent(Student student) throws SQLException {
        if (student.getCode() == null || student.getCode().isBlank()) {
            throw new IllegalArgumentException("Student code is required for update.");
        }
        studentDAO.update(student);
    }

    public void deleteStudent(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Student code is required for deletion.");
        }
        studentDAO.delete(code);
    }

    public Student getStudentByCode(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Student code is required.");
        }
        return studentDAO.findByCode(code);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.findAll();
    }

    public List<Student> getStudentsByCourse(String courseCode) throws SQLException {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code is required.");
        }
        return studentDAO.findByCourseCode(courseCode);
    }
}
