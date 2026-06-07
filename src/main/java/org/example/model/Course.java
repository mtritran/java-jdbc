package org.example.model;

public class Course {
    private String code;
    private String name;
    private int credits;
    private int maxStudents;
    private int registeredCount;
    private String lecturerCode;

    public Course() {
    }

    public Course(String code, String name, int credits, int maxStudents, String lecturerCode) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.registeredCount = 0;
        this.lecturerCode = lecturerCode;
    }

    public Course(String code, String name, int credits, int maxStudents, int registeredCount, String lecturerCode) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.maxStudents = maxStudents;
        this.registeredCount = registeredCount;
        this.lecturerCode = lecturerCode;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getRegisteredCount() {
        return registeredCount;
    }

    public void setRegisteredCount(int registeredCount) {
        this.registeredCount = registeredCount;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", maxStudents=" + maxStudents +
                ", registeredCount=" + registeredCount +
                ", lecturerCode='" + lecturerCode + '\'' +
                '}';
    }
}
