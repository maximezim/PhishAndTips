package com.learnandphish.authentication.user;

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private Roles role;

    public RegisterRequest() {}

    public RegisterRequest(String firstName, String lastName, String email, String position, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public Roles getRole() {
        return role;
    }

}
