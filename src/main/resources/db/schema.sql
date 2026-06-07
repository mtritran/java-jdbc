DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS lecturers CASCADE;

CREATE TABLE lecturers (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    dob DATE NOT NULL,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE students (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    dob DATE NOT NULL,
    major VARCHAR(100) NOT NULL,
    gpa NUMERIC(3, 2) NOT NULL
);

CREATE TABLE courses (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    max_students INT NOT NULL,
    registered_count INT DEFAULT 0,
    lecturer_code VARCHAR(50),
    FOREIGN KEY (lecturer_code) REFERENCES lecturers(code) ON DELETE SET NULL
);

CREATE TABLE enrollments (
    student_code VARCHAR(50),
    course_code VARCHAR(50),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_code, course_code),
    FOREIGN KEY (student_code) REFERENCES students(code) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
);
