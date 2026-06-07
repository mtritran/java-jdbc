package org.example.model;

import java.time.LocalDate;

public class Lecturer extends Person {
    private String department;

    public Lecturer() {
        super();
    }

    public Lecturer(String code, String name, String email, LocalDate dob, String department) {
        super(code, name, email, dob);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "code='" + getCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", dob=" + getDob() +
                ", department='" + department + '\'' +
                '}';
    }
}
