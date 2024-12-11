package com.learnandphish.authentication;

public class UserDTO {

        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String position;
        private Roles role;

        public UserDTO() {}

        public UserDTO(int id, String firstName, String lastName, String email, String position, Roles role) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.position = position;
            this.role = role;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setRole(Roles role) {
            this.role = role;
        }

}
