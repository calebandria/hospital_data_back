package com.example.demo.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank 
    private String password;
    @NotBlank
    private String role;


}
