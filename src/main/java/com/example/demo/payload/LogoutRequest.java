package com.example.demo.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LogoutRequest {
    @NotBlank
    private String refreshToken;
}
