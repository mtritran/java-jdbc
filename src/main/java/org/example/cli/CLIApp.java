package org.example.cli;

import org.example.model.Course;
import org.example.model.Lecturer;
import org.example.model.Student;
import org.example.service.CourseService;
import org.example.service.LecturerService;
import org.example.service.RegistrationService;
import org.example.service.StudentService;
import org.example.simulation.ConcurrencySimulation;
import org.example.util.DBConnection;
import org.example.util.DDLRunner;
import org.example.util.FileImportUtil;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

public class CLIApp {
    private final StudentService studentService;
    private final LecturerService lecturerService;
    private final CourseService courseService;
    private final RegistrationService registrationService;

    public CLIApp() {
        this.studentService = new StudentService();
        this.lecturerService = new LecturerService();
        this.courseService = new CourseService();
        this.registrationService = new RegistrationService();
    }

    public void start() {
        DDLRunner.runSchemaScript();

        while (true) {
            System.out.println("\n=== UNIVERSITY MANAGEMENT SYSTEM ===");
            System.out.println("1. Bulk Import Data from CSVs");
            System.out.println("2. Student Management");
            System.out.println("3. Lecturer Management");
            System.out.println("4. Course Management");
            System.out.println("5. Register Course for Student");
            System.out.println("6. Run Concurrency Simulation");
            System.out.println("7. Reports");
            System.out.println("8. Reset Database");
            System.out.println("9. Exit");

            int choice = ConsoleInputHelper.readInt("Enter choice (1-9): ");
            switch (choice) {
                case 1 -> bulkImport();
                case 2 -> studentMenu();
                case 3 -> lecturerMenu();
                case 4 -> courseMenu();
                case 5 -> registerCourse();
                case 6 -> runConcurrencySimulation();
                case 7 -> reportsMenu();
                case 8 -> resetDatabase();
                case 9 -> {
                    System.out.println("Exiting system. Goodbye!");
                    DBConnection.shutdown();
                    return;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }
    }

    private void studentMenu() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Find Student by Code");
            System.out.println("5. Display All Students");
            System.out.println("6. Back to Main Menu");

            int choice = ConsoleInputHelper.readInt("Enter choice (1-6): ");
            try {
                switch (choice) {
                    case 1 -> {
                        String code = ConsoleInputHelper.readString("Enter student code (e.g. SV016): ");
                        String name = ConsoleInputHelper.readString("Enter student name: ");
                        String email = ConsoleInputHelper.readString("Enter student email: ");
                        LocalDate dob = ConsoleInputHelper.readDate("Enter date of birth");
                        String major = ConsoleInputHelper.readString("Enter major: ");
                        double gpa = ConsoleInputHelper.readDouble("Enter GPA (0.0 - 4.0): ");
                        Student student = new Student(code, name, email, dob, major, gpa);
                        studentService.addStudent(student);
                        System.out.println("Student added successfully!");
                    }
                    case 2 -> {
                        String code = ConsoleInputHelper.readString("Enter student code to update: ");
                        Student existing = studentService.getStudentByCode(code);
                        if (existing == null) {
                            System.out.println("Student not found.");
                            break;
                        }
                        String name = ConsoleInputHelper.readString("Enter new name (" + existing.getName() + "): ");
                        String email = ConsoleInputHelper.readString("Enter new email (" + existing.getEmail() + "): ");
                        LocalDate dob = ConsoleInputHelper.readDate("Enter new date of birth (" + existing.getDob() + ")");
                        String major = ConsoleInputHelper.readString("Enter new major (" + existing.getMajor() + "): ");
                        double gpa = ConsoleInputHelper.readDouble("Enter new GPA (" + existing.getGpa() + "): ");
                        
                        Student student = new Student(code, 
                            name.isBlank() ? existing.getName() : name, 
                            email.isBlank() ? existing.getEmail() : email, 
                            dob, 
                            major.isBlank() ? existing.getMajor() : major, 
                            gpa);
                        studentService.updateStudent(student);
                        System.out.println("Student updated successfully!");
                    }
                    case 3 -> {
                        String code = ConsoleInputHelper.readString("Enter student code to delete: ");
                        studentService.deleteStudent(code);
                        System.out.println("Student deleted successfully!");
                    }
                    case 4 -> {
                        String code = ConsoleInputHelper.readString("Enter student code: ");
                        Student student = studentService.getStudentByCode(code);
                        if (student != null) {
                            System.out.println(student);
                        } else {
                            System.out.println("Student not found.");
                        }
                    }
                    case 5 -> {
                        List<Student> students = studentService.getAllStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 6 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void lecturerMenu() {
        while (true) {
            System.out.println("\n--- Lecturer Management ---");
            System.out.println("1. Add Lecturer");
            System.out.println("2. Update Lecturer");
            System.out.println("3. Delete Lecturer");
            System.out.println("4. Find Lecturer by Code");
            System.out.println("5. Display All Lecturers");
            System.out.println("6. Back to Main Menu");

            int choice = ConsoleInputHelper.readInt("Enter choice (1-6): ");
            try {
                switch (choice) {
                    case 1 -> {
                        String code = ConsoleInputHelper.readString("Enter lecturer code (e.g. GV007): ");
                        String name = ConsoleInputHelper.readString("Enter lecturer name: ");
                        String email = ConsoleInputHelper.readString("Enter lecturer email: ");
                        LocalDate dob = ConsoleInputHelper.readDate("Enter date of birth");
                        String dept = ConsoleInputHelper.readString("Enter department: ");
                        Lecturer lecturer = new Lecturer(code, name, email, dob, dept);
                        lecturerService.addLecturer(lecturer);
                        System.out.println("Lecturer added successfully!");
                    }
                    case 2 -> {
                        String code = ConsoleInputHelper.readString("Enter lecturer code to update: ");
                        Lecturer existing = lecturerService.getLecturerByCode(code);
                        if (existing == null) {
                            System.out.println("Lecturer not found.");
                            break;
                        }
                        String name = ConsoleInputHelper.readString("Enter new name (" + existing.getName() + "): ");
                        String email = ConsoleInputHelper.readString("Enter new email (" + existing.getEmail() + "): ");
                        LocalDate dob = ConsoleInputHelper.readDate("Enter new date of birth (" + existing.getDob() + ")");
                        String dept = ConsoleInputHelper.readString("Enter new department (" + existing.getDepartment() + "): ");
                        
                        Lecturer lecturer = new Lecturer(code, 
                            name.isBlank() ? existing.getName() : name, 
                            email.isBlank() ? existing.getEmail() : email, 
                            dob, 
                            dept.isBlank() ? existing.getDepartment() : dept);
                        lecturerService.updateLecturer(lecturer);
                        System.out.println("Lecturer updated successfully!");
                    }
                    case 3 -> {
                        String code = ConsoleInputHelper.readString("Enter lecturer code to delete: ");
                        lecturerService.deleteLecturer(code);
                        System.out.println("Lecturer deleted successfully!");
                    }
                    case 4 -> {
                        String code = ConsoleInputHelper.readString("Enter lecturer code: ");
                        Lecturer lecturer = lecturerService.getLecturerByCode(code);
                        if (lecturer != null) {
                            System.out.println(lecturer);
                        } else {
                            System.out.println("Lecturer not found.");
                        }
                    }
                    case 5 -> {
                        List<Lecturer> lecturers = lecturerService.getAllLecturers();
                        if (lecturers.isEmpty()) {
                            System.out.println("No lecturers found.");
                        } else {
                            lecturers.forEach(System.out::println);
                        }
                    }
                    case 6 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void courseMenu() {
        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. Find Course by Code");
            System.out.println("5. Display All Courses");
            System.out.println("6. Back to Main Menu");

            int choice = ConsoleInputHelper.readInt("Enter choice (1-6): ");
            try {
                switch (choice) {
                    case 1 -> {
                        String code = ConsoleInputHelper.readString("Enter course code (e.g. CS203): ");
                        String name = ConsoleInputHelper.readString("Enter course name: ");
                        int credits = ConsoleInputHelper.readInt("Enter course credits: ");
                        int capacity = ConsoleInputHelper.readInt("Enter max students capacity: ");
                        String lecturerCode = ConsoleInputHelper.readString("Enter lecturer code (leave blank for none): ");
                        if (lecturerCode.isBlank()) lecturerCode = null;
                        
                        Course course = new Course(code, name, credits, capacity, lecturerCode);
                        courseService.addCourse(course);
                        System.out.println("Course added successfully!");
                    }
                    case 2 -> {
                        String code = ConsoleInputHelper.readString("Enter course code to update: ");
                        Course existing = courseService.getCourseByCode(code);
                        if (existing == null) {
                            System.out.println("Course not found.");
                            break;
                        }
                        String name = ConsoleInputHelper.readString("Enter new name (" + existing.getName() + "): ");
                        int credits = ConsoleInputHelper.readInt("Enter new credits (" + existing.getCredits() + "): ");
                        int capacity = ConsoleInputHelper.readInt("Enter new capacity (" + existing.getMaxStudents() + "): ");
                        String lecturerCode = ConsoleInputHelper.readString("Enter new lecturer code (" + existing.getLecturerCode() + "): ");
                        if (lecturerCode.isBlank()) lecturerCode = existing.getLecturerCode();
                        
                        Course course = new Course(code, 
                            name.isBlank() ? existing.getName() : name, 
                            credits, 
                            capacity, 
                            existing.getRegisteredCount(), 
                            lecturerCode);
                        courseService.updateCourse(course);
                        System.out.println("Course updated successfully!");
                    }
                    case 3 -> {
                        String code = ConsoleInputHelper.readString("Enter course code to delete: ");
                        courseService.deleteCourse(code);
                        System.out.println("Course deleted successfully!");
                    }
                    case 4 -> {
                        String code = ConsoleInputHelper.readString("Enter course code: ");
                        Course course = courseService.getCourseByCode(code);
                        if (course != null) {
                            System.out.println(course);
                        } else {
                            System.out.println("Course not found.");
                        }
                    }
                    case 5 -> {
                        List<Course> courses = courseService.getAllCourses();
                        if (courses.isEmpty()) {
                            System.out.println("No courses found.");
                        } else {
                            courses.forEach(System.out::println);
                        }
                    }
                    case 6 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerCourse() {
        System.out.println("\n--- Register Course for Student ---");
        String studentCode = ConsoleInputHelper.readString("Enter student code: ");
        String courseCode = ConsoleInputHelper.readString("Enter course code: ");
        try {
            registrationService.enrollStudent(studentCode, courseCode);
            System.out.println("Registration successful! Student has been enrolled in the course.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void bulkImport() {
        System.out.println("\n--- Bulk Import Data from CSVs ---");
        try {
            FileImportUtil importer = new FileImportUtil();
            
            InputStream lecturerStream = CLIApp.class.getClassLoader().getResourceAsStream("import_data/lecturers.csv");
            int lecturersCount = importer.importLecturers(lecturerStream);
            System.out.println("Imported " + lecturersCount + " new lecturers.");

            InputStream studentStream = CLIApp.class.getClassLoader().getResourceAsStream("import_data/students.csv");
            int studentsCount = importer.importStudents(studentStream);
            System.out.println("Imported " + studentsCount + " new students.");

            InputStream courseStream = CLIApp.class.getClassLoader().getResourceAsStream("import_data/courses.csv");
            int coursesCount = importer.importCourses(courseStream);
            System.out.println("Imported " + coursesCount + " new courses.");
            
            System.out.println("Bulk import completed successfully!");
        } catch (Exception e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }

    private void runConcurrencySimulation() {
        ConcurrencySimulation simulation = new ConcurrencySimulation();
        simulation.runSimulation();
    }

    private void reportsMenu() {
        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1. List students in a specific course");
            System.out.println("2. List courses taught by a lecturer");
            System.out.println("3. General statistics");
            System.out.println("4. Back to Main Menu");

            int choice = ConsoleInputHelper.readInt("Enter choice (1-4): ");
            try {
                switch (choice) {
                    case 1 -> {
                        String courseCode = ConsoleInputHelper.readString("Enter course code: ");
                        Course course = courseService.getCourseByCode(courseCode);
                        if (course == null) {
                            System.out.println("Course not found.");
                            break;
                        }
                        System.out.println("Course: " + course.getName() + " (" + courseCode + ")");
                        System.out.println("Registered count: " + course.getRegisteredCount() + " / " + course.getMaxStudents());
                        List<Student> students = studentService.getStudentsByCourse(courseCode);
                        if (students.isEmpty()) {
                            System.out.println("No students enrolled in this course.");
                        } else {
                            System.out.println("Enrolled Students list:");
                            students.forEach(s -> System.out.printf(" - %s: %s (%s, GPA: %.2f)\n", s.getCode(), s.getName(), s.getMajor(), s.getGpa()));
                        }
                    }
                    case 2 -> {
                        String lecturerCode = ConsoleInputHelper.readString("Enter lecturer code: ");
                        Lecturer lecturer = lecturerService.getLecturerByCode(lecturerCode);
                        if (lecturer == null) {
                            System.out.println("Lecturer not found.");
                            break;
                        }
                        System.out.println("Lecturer: " + lecturer.getName() + " (" + lecturerCode + ")");
                        List<Course> courses = courseService.getCoursesByLecturer(lecturerCode);
                        if (courses.isEmpty()) {
                            System.out.println("This lecturer is not teaching any courses.");
                        } else {
                            System.out.println("Assigned Courses:");
                            courses.forEach(c -> System.out.printf(" - %s: %s (%d credits, capacity: %d, registered: %d)\n", c.getCode(), c.getName(), c.getCredits(), c.getMaxStudents(), c.getRegisteredCount()));
                        }
                    }
                    case 3 -> {
                        int studentsSize = studentService.getAllStudents().size();
                        int lecturersSize = lecturerService.getAllLecturers().size();
                        int coursesSize = courseService.getAllCourses().size();
                        
                        System.out.println("General System Statistics:");
                        System.out.println(" - Total Students: " + studentsSize);
                        System.out.println(" - Total Lecturers: " + lecturersSize);
                        System.out.println(" - Total Courses: " + coursesSize);
                    }
                    case 4 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error generating report: " + e.getMessage());
            }
        }
    }

    private void resetDatabase() {
        String confirm = ConsoleInputHelper.readString("Are you sure you want to reset all data? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            DDLRunner.runSchemaScript();
            System.out.println("Database reset completed successfully. All data has been cleared.");
        } else {
            System.out.println("Reset cancelled.");
        }
    }
}
