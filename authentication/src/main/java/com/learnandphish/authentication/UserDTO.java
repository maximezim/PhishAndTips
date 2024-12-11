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

        public int getId() {
            return id;
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
