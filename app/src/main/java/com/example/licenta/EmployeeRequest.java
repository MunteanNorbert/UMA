package com.example.licenta;

public class EmployeeRequest {
    private String message;
    private String name;
    private String userID;
    private String email;
    private String date;
    private String day;
    private String time;

    public EmployeeRequest() { }

    public EmployeeRequest(String message, String name, String userID, String email, String date, String day, String time) {
        this.message = message;
        this.name = name;
        this.userID = userID;
        this.email = email;
        this.date = date;
        this.day = day;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

