package com.example.licenta;

public class Student {
    private String firstname;
    private String lastname;
    private String email;
    private String year;
    private String userID;


    public Student() {}

    public Student(String firstname, String lastname, String email, String year, String userID) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.year = year;
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}