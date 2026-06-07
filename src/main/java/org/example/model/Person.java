package org.example.model;

import java.time.LocalDate;

public abstract class Person {
    protected String code;
    protected String name;
    protected String email;
    protected LocalDate dob;

    public Person() {
    }

    public Person(String code, String name, String email, LocalDate dob) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
