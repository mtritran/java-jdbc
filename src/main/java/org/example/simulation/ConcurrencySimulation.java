package org.example.simulation;

import org.example.model.Course;
import org.example.model.Student;
import org.example.service.CourseService;
import org.example.service.RegistrationService;
import org.example.service.StudentService;
import org.example.util.DBConnection;
import org.example.util.DDLRunner;
import org.example.util.FileImportUtil;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencySimulation {
    private final RegistrationService registrationService;
    private final CourseService courseService;
    private final StudentService studentService;

    public ConcurrencySimulation() {
        this.registrationService = new RegistrationService();
        this.courseService = new CourseService();
        this.studentService = new StudentService();
    }

    public void runSimulation() {
        System.out.println("\n==================================================");
        System.out.println("=== STARTING CONCURRENCY REGISTRATION SIMULATION ===");
        System.out.println("==================================================");

        // 1. Reset database and import fresh sample data
        DDLRunner.runSchemaScript();
        try {
            System.out.println("Importing fresh CSV data...");
            FileImportUtil importer = new FileImportUtil();
            
            InputStream lecturerStream = ConcurrencySimulation.class.getClassLoader().getResourceAsStream("import_data/lecturers.csv");
            importer.importLecturers(lecturerStream);

            InputStream studentStream = ConcurrencySimulation.class.getClassLoader().getResourceAsStream("import_data/students.csv");
            importer.importStudents(studentStream);

            InputStream courseStream = ConcurrencySimulation.class.getClassLoader().getResourceAsStream("import_data/courses.csv");
            importer.importCourses(courseStream);

        } catch (Exception e) {
            System.err.println("Failed to import data for simulation: " + e.getMessage());
            return;
        }

        // Target course CS102 (Capacity: 2 slots)
        String courseCode = "CS102";
        
        // 10 students trying to register simultaneously
        List<String> studentCodes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            studentCodes.add(String.format("SV%03d", i)); // SV001 to SV010
        }

        try {
            Course course = courseService.getCourseByCode(courseCode);
            System.out.println("\nTarget Course Details:");
            System.out.println(" - Code: " + course.getCode());
            System.out.println(" - Name: " + course.getName());
            System.out.println(" - Max Students Capacity: " + course.getMaxStudents());
            System.out.println(" - Initial Registered Count: " + course.getRegisteredCount());
            System.out.println("Number of simulated students attempting to register: " + studentCodes.size());
            System.out.println("\nSpawning 10 threads...");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Thread synchronization utilities
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(studentCodes.size());
        
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger courseFullCounter = new AtomicInteger(0);
        AtomicInteger otherErrorCounter = new AtomicInteger(0);

        for (String studentCode : studentCodes) {
            new Thread(() -> {
                try {
                    // Wait for the starter gun
                    startLatch.await();
                    
                    // Attempt course registration
                    registrationService.enrollStudent(studentCode, courseCode);
                    
                    // If successful
                    successCounter.incrementAndGet();
                    System.out.printf("[SUCCESS] Student %s successfully enrolled in course %s.\n", studentCode, courseCode);
                } catch (IllegalStateException e) {
                    if (e.getMessage().contains("full")) {
                        courseFullCounter.incrementAndGet();
                        System.out.printf("[FAILED - COURSE FULL] Student %s failed to enroll in %s: %s\n", studentCode, courseCode, e.getMessage());
                    } else {
                        otherErrorCounter.incrementAndGet();
                        System.out.printf("[FAILED - ERROR] Student %s failed: %s\n", studentCode, e.getMessage());
                    }
                } catch (IllegalArgumentException | SQLException e) {
                    otherErrorCounter.incrementAndGet();
                    System.out.printf("[FAILED - ERROR] Student %s failed: %s\n", studentCode, e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }

        // Fire the starter gun!
        System.out.println("\n>>> TRIGGERING SIMULTANEOUS REGISTRATION NOW! <<<\n");
        startLatch.countDown();

        try {
            // Wait for all threads to complete
            endLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Simulation interrupted.");
        }

        // 3. Print results and verify integrity
        System.out.println("\n==================================================");
        System.out.println("===             SIMULATION RESULTS             ===");
        System.out.println("==================================================");
        System.out.println("Total attempts: " + studentCodes.size());
        System.out.println("Successful enrollments: " + successCounter.get());
        System.out.println("Blocked due to full course: " + courseFullCounter.get());
        System.out.println("Other errors: " + otherErrorCounter.get());

        try {
            Course courseAfter = courseService.getCourseByCode(courseCode);
            System.out.println("\nVerification in Database:");
            System.out.println(" - Course Code: " + courseAfter.getCode());
            System.out.println(" - Final Registered Count in Database: " + courseAfter.getRegisteredCount());
            System.out.println(" - Capacity Limit: " + courseAfter.getMaxStudents());

            List<Student> enrolledStudents = studentService.getStudentsByCourse(courseCode);
            System.out.println(" - List of students enrolled in " + courseCode + " in Database:");
            enrolledStudents.forEach(s -> System.out.println("   * " + s.getCode() + " - " + s.getName()));

            if (courseAfter.getRegisteredCount() == courseAfter.getMaxStudents() && enrolledStudents.size() == courseAfter.getMaxStudents()) {
                System.out.println("\nINTEGRITY CHECK: PASSED!");
                System.out.println("Database remains 100% consistent. Pessimistic locking successfully prevented over-enrollment!");
            } else {
                System.out.println("\nINTEGRITY CHECK: FAILED!");
                System.out.println("Database count is inconsistent with course capacity or enrollments table!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to query database for verification: " + e.getMessage());
        }
        System.out.println("==================================================\n");
    }

    public static void main(String[] args) {
        try {
            new ConcurrencySimulation().runSimulation();
        } finally {
            DBConnection.shutdown();
        }
    }
}
