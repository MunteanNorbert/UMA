package com.example.licenta;

public class StudentRequests {
    private String request;
    private String username;
    private String selectedSubject;
    private String professorUserID;

    public StudentRequests() {}

    public StudentRequests(String request, String username, String selectedSubject, String professorUserID) {
        this.request = request;
        this.username = username;
        this.selectedSubject = selectedSubject;
        this.professorUserID = professorUserID;
    }

    public String getRequest() { return request; }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getSelectedSubject() { return selectedSubject; }

    public void setSelectedSubject(String selectedSubject) { this.selectedSubject = selectedSubject; }

    public String getProfessorUserID() { return professorUserID; }

    public void setProfessorUserID(String professorUserID) { this.professorUserID = professorUserID; }

}