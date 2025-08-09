package com.example.demo.payload;

import lombok.Data;

@Data
public class LoginRequest {
    

    private String username;
   
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "LoginRequest{" +
               "username='" + username + '\'' +
               '}'; // Don't print password for security reasonsusern
    }
}
