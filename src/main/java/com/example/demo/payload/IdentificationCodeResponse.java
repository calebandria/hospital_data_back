package com.example.demo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentificationCodeResponse
 {
    private String role;
    private long identification;
}
