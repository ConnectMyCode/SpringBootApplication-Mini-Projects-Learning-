package org.spring.core.learn.spring14fitclubmvc.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * ================= REST API: @RequestBody DTO =================
 * The React frontend sends JSON in the request body, e.g.:
 *   { "username": "ravi_k", "email": "ravi@example.com", "password": "secret123" }
 * The controller binds this with @RequestBody. @RequestBody is just the
 * SIGNAL - the actual JSON -> Java conversion is done by an
 * HttpMessageConverter (Jackson) BEFORE the controller method runs.
 * @Valid still triggers the Jakarta Bean Validation annotations below.
 */
public class MemberRegistrationRequest {


@NotBlank(message = "Username is required")
@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
private String username;


@NotBlank(message = "Email is required")
@Email(message = "Enter a valid email adress")
private String email;


@NotBlank(message="Password is required")
@Size(min = 6, max = 100, message = "Password must be at least 6 characters")
private String password;


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }



}
