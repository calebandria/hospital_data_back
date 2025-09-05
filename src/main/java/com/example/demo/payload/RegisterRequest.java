package com.example.demo.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank 
    private String password;
    @NotBlank
    private String role;
    @NotNull
    private long identification;
}
