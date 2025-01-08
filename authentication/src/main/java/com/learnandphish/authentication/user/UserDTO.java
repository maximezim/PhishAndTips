package com.learnandphish.authentication.user;

import lombok.Data;

@Data
public class UserDTO {

        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String position;
        private Roles role;

}
