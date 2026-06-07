package org.example.util;

import org.example.model.Course;
import org.example.model.Lecturer;
import org.example.model.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReportExporter {

    private static void ensureParentDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    public static void exportCourseReport(String filePath, Course course, List<Student> students) throws IOException {
        ensureParentDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write("=== COURSE REPORT ===\n");
            writer.write("Course: " + course.getName() + " (" + course.getCode() + ")\n");
            writer.write("Credits: " + course.getCredits() + "\n");
            writer.write("Registered count: " + course.getRegisteredCount() + " / " + course.getMaxStudents() + "\n");
            writer.write("=====================\n");
            writer.write("Enrolled Students:\n");
            if (students.isEmpty()) {
                writer.write("No students enrolled in this course.\n");
            } else {
                for (Student s : students) {
                    writer.write(String.format(" - %s: %s (%s, GPA: %.2f)\n", s.getCode(), s.getName(), s.getMajor(), s.getGpa()));
                }
            }
        }
    }

    public static void exportLecturerReport(String filePath, Lecturer lecturer, List<Course> courses) throws IOException {
        ensureParentDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write("=== LECTURER REPORT ===\n");
            writer.write("Lecturer: " + lecturer.getName() + " (" + lecturer.getCode() + ")\n");
            writer.write("Email: " + lecturer.getEmail() + "\n");
            writer.write("Department: " + lecturer.getDepartment() + "\n");
            writer.write("=======================\n");
            writer.write("Assigned Courses:\n");
            if (courses.isEmpty()) {
                writer.write("This lecturer is not teaching any courses.\n");
            } else {
                for (Course c : courses) {
                    writer.write(String.format(" - %s: %s (%d credits, capacity: %d, registered: %d)\n", c.getCode(), c.getName(), c.getCredits(), c.getMaxStudents(), c.getRegisteredCount()));
                }
            }
        }
    }

    public static void exportSystemStatistics(String filePath, int totalStudents, int totalLecturers, int totalCourses) throws IOException {
        ensureParentDirectoryExists(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            writer.write("=== GENERAL SYSTEM STATISTICS ===\n");
            writer.write("Total Students: " + totalStudents + "\n");
            writer.write("Total Lecturers: " + totalLecturers + "\n");
            writer.write("Total Courses: " + totalCourses + "\n");
            writer.write("=================================\n");
        }
    }
}
