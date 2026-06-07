package org.example.service;

import org.example.dao.CourseDAO;
import org.example.model.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    public void addCourse(Course course) throws SQLException {
        if (course.getCode() == null || course.getCode().isBlank()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        if (course.getName() == null || course.getName().isBlank()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        if (course.getCredits() <= 0) {
            throw new IllegalArgumentException("Course credits must be positive.");
        }
        if (course.getMaxStudents() <= 0) {
            throw new IllegalArgumentException("Course max students capacity must be positive.");
        }
        courseDAO.insert(course);
    }

    public void updateCourse(Course course) throws SQLException {
        if (course.getCode() == null || course.getCode().isBlank()) {
            throw new IllegalArgumentException("Course code is required for update.");
        }
        courseDAO.update(course);
    }

    public void deleteCourse(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Course code is required for deletion.");
        }
        courseDAO.delete(code);
    }

    public Course getCourseByCode(String code) throws SQLException {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Course code is required.");
        }
        return courseDAO.findByCode(code);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.findAll();
    }

    public List<Course> getCoursesByLecturer(String lecturerCode) throws SQLException {
        if (lecturerCode == null || lecturerCode.isBlank()) {
            throw new IllegalArgumentException("Lecturer code is required.");
        }
        return courseDAO.findByLecturerCode(lecturerCode);
    }
}
