package com.learnandphish.authentication.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

        private String firstName;
        private String lastName;
        private String email;
        private String position;
        private Roles role;

}
