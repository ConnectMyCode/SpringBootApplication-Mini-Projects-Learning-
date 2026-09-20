package org.spring.core.learn.spring14fitclubmvc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * ================= FROM YOUR CONTEXT (Bean Validation) =================
 * "To perform input validation in a Spring MVC/Spring REST application,
 *  we have to use Jakarta Bean validation api."
 * "Bean validation api is a set of annotations to perform validations,
 *  without writing manual validation logic."
 * "The starter, spring-boot-starter-validation brings the Bean validation
 *  with Hibernate validator into the application classpath."
 *
 * This is a dedicated form-backing object (separate from the Member
 * entity) - a common real-world pattern: you never want the entity that's
 * mapped straight to your database to also be the object bound directly
 * from raw HTTP form input. Keeping them separate means adding a
 * validation rule here never accidentally changes your database schema,
 * and vice versa.
 */
public class MemberRegistrationForm {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
