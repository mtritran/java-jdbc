package org.example.util;

import org.example.model.Course;
import org.example.model.Lecturer;
import org.example.model.Student;
import org.example.service.CourseService;
import org.example.service.LecturerService;
import org.example.service.StudentService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileImportUtil {
    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final CourseService courseService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FileImportUtil() {
        this.studentService = new StudentService();
        this.lecturerService = new LecturerService();
        this.courseService = new CourseService();
    }

    public int importLecturers(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        int successCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;

                String code = tokens[0].trim();
                String name = tokens[1].trim();
                String email = tokens[2].trim();
                LocalDate dob = LocalDate.parse(tokens[3].trim(), DATE_FORMATTER);
                String department = tokens[4].trim();

                if (lecturerService.getLecturerByCode(code) == null) {
                    Lecturer lecturer = new Lecturer(code, name, email, dob, department);
                    lecturerService.addLecturer(lecturer);
                    successCount++;
                }
            }
        }
        return successCount;
    }

    public int importStudents(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        int successCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 6) continue;

                String code = tokens[0].trim();
                String name = tokens[1].trim();
                String email = tokens[2].trim();
                LocalDate dob = LocalDate.parse(tokens[3].trim(), DATE_FORMATTER);
                String major = tokens[4].trim();
                double gpa = Double.parseDouble(tokens[5].trim());

                if (studentService.getStudentByCode(code) == null) {
                    Student student = new Student(code, name, email, dob, major, gpa);
                    studentService.addStudent(student);
                    successCount++;
                }
            }
        }
        return successCount;
    }

    public int importCourses(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        int successCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;
                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;

                String code = tokens[0].trim();
                String name = tokens[1].trim();
                int credits = Integer.parseInt(tokens[2].trim());
                int maxStudents = Integer.parseInt(tokens[3].trim());
                String lecturerCode = tokens[4].trim();
                if (lecturerCode.equalsIgnoreCase("null") || lecturerCode.isBlank()) {
                    lecturerCode = null;
                }

                if (courseService.getCourseByCode(code) == null) {
                    Course course = new Course(code, name, credits, maxStudents, lecturerCode);
                    courseService.addCourse(course);
                    successCount++;
                }
            }
        }
        return successCount;
    }
}
