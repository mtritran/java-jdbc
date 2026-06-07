package org.example.model;

import java.time.LocalDate;

public class Student extends Person {
    private String major;
    private double gpa;

    public Student() {
        super();
    }

    public Student(String code, String name, String email, LocalDate dob, String major, double gpa) {
        super(code, name, email, dob);
        this.major = major;
        this.gpa = gpa;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "code='" + getCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", dob=" + getDob() +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}
