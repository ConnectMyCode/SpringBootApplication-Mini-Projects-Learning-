package org.Spring.core.dto;

public class UserDTO {

    // DTO = Data Transfer Object
// Purpose: carry data between layers (Controller → Service → Response)
// It is a PLAIN Java class — NO @Component, NOT managed by Spring
// You create it yourself with "new", Spring doesn't touch it

        private String name;
        private String email;
        private String role;

        // Constructor
        public UserDTO(String name, String email, String role) {
            this.name = name;
            this.email = email;
            this.role = role;
        }

        // Getters
        public String getName()  { return name; }
        public String getEmail() { return email; }
        public String getRole()  { return role; }

        @Override
        public String toString() {
            return "UserDTO{name='" + name + "', email='" + email + "', role='" + role + "'}";
        }
    }
/*
What is a DTO? Imagine your database
 User entity has 20 fields including password, ssn, internalId.
 You don't want to send all that to the client.
 A DTO is a trimmed copy — only the fields the client actually needs.
  It's just a plain Java class, not a Spring bean.
* */
