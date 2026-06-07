package org.example.model;

import java.time.LocalDateTime;

public class Enrollment {
    private String studentCode;
    private String courseCode;
    private LocalDateTime enrollmentDate;

    public Enrollment() {
    }

    public Enrollment(String studentCode, String courseCode) {
        this.studentCode = studentCode;
        this.courseCode = courseCode;
        this.enrollmentDate = LocalDateTime.now();
    }

    public Enrollment(String studentCode, String courseCode, LocalDateTime enrollmentDate) {
        this.studentCode = studentCode;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "studentCode='" + studentCode + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
