package com.learnandphish.authentication.jwt;

import lombok.Data;

@Data
public class JwtRequest {

    private String email;
    private String password;

}

