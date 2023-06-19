package com.example.licenta;

public class Subject {
    private String subjectName;
    private String year;
    private String professorFirstName;
    private String professorLastName;
    private String professorUserName;

    public Subject() {}

    public Subject(String subjectName, String year, String professorFirstName, String professorLastName, String professorUserName) {
        this.subjectName = subjectName;
        this.year = year;
        this.professorFirstName = professorFirstName;
        this.professorLastName = professorLastName;
        this.professorUserName = professorUserName;
    }

    public void setName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getName() { return subjectName; }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() { return year; }

    public void setProfessorFirstName(String professorFirstName) {
        this.professorFirstName = professorFirstName;
    }

    public String getProfessorFirstName() { return professorFirstName; }

    public void setProfessorLastName(String professorLastName) {
        this.professorLastName = professorLastName;
    }

    public String getProfessorLastName() { return professorLastName; }

    public void setProfessorUserName(String professorUserName) {
        this.professorUserName = professorUserName;
    }

    public String getProfessorUserName() { return professorUserName; }
}
