package com.example.licenta;

public class StudentAttendance {
    private String message;
    private String date;
    private String time;
    private String studentUserID;

    public StudentAttendance() {}

    public StudentAttendance(String message, String date, String time, String studentUserID) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.studentUserID = studentUserID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStudentUserID() { return studentUserID; }

    public void setStudentUserID(String studentUserID) { this.studentUserID = studentUserID; }
}