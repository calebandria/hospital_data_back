package com.example.demo.payload;

import lombok.Data;

@Data
public class IdentificationRegistration {   

    private long identification;

    private String role;

    public IdentificationRegistration(long identification){
        this.identification = identification;
    }
    
}
