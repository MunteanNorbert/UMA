package com.example.licenta;

public class Person {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String category;

    public Person() { }

    public Person(String userName, String firstName, String lastName, String email, String category) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.category = category;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

}

