package org.example.service;

import org.example.dao.CourseDAO;
import org.example.dao.EnrollmentDAO;
import org.example.dao.StudentDAO;
import org.example.model.Course;
import org.example.model.Enrollment;
import org.example.model.Student;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationService {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;

    public RegistrationService() {
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
        this.enrollmentDAO = new EnrollmentDAO();
    }

    /**
     * Enrolls a student in a course. Handles concurrency using Pessimistic Locking (FOR UPDATE).
     */
    public void enrollStudent(String studentCode, String courseCode) throws SQLException {
        if (studentCode == null || studentCode.isBlank()) {
            throw new IllegalArgumentException("Student code is required.");
        }
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code is required.");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check if Student exists
            Student student = studentDAO.findByCode(studentCode);
            if (student == null) {
                throw new IllegalArgumentException("Student with code " + studentCode + " not found.");
            }

            // 2. Fetch and lock Course record using FOR UPDATE
            Course course = courseDAO.findByCodeForUpdate(courseCode, conn);
            if (course == null) {
                throw new IllegalArgumentException("Course with code " + courseCode + " not found.");
            }

            // 3. Check if Student is already enrolled
            boolean isAlreadyEnrolled = enrollmentDAO.isEnrolled(studentCode, courseCode, conn);
            if (isAlreadyEnrolled) {
                throw new IllegalStateException("Student " + studentCode + " is already enrolled in course " + courseCode + ".");
            }

            // 4. Check if Course capacity is full
            if (course.getRegisteredCount() >= course.getMaxStudents()) {
                throw new IllegalStateException("Course " + courseCode + " is full (Capacity: " + course.getMaxStudents() + ").");
            }

            // 5. Insert enrollment record
            Enrollment enrollment = new Enrollment(studentCode, courseCode);
            enrollmentDAO.insert(enrollment, conn);

            // 6. Update course registered count
            courseDAO.incrementRegisteredCount(courseCode, conn);

            // Commit the transaction
            conn.commit();
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (e instanceof SQLException) throw (SQLException) e;
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException("Unexpected error during enrollment: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset connection state before closing
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
