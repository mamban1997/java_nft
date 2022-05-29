package com.example.crypto.security.model;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class RegisterDto {
    private String username;
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "email is invalid")
    private String email;
    private String password;
}
