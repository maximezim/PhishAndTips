package com.learnandphish.authentication.user;

import lombok.Data;

@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private Roles role;

}
