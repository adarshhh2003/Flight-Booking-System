package com.security.entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#!$%&*])[A-Za-z\\d@#!$%&*]{6,}$",
//            message = "Password must be at least 6 character long and contains uppercase, lowercase, digit, special character"
//    )
    private String password;
}
