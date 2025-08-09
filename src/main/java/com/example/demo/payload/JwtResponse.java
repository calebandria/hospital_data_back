package com.example.demo.payload;
import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    private String username;
    private String role;

    public JwtResponse(String accessToken){
        this.accessToken = accessToken;
    }

    public JwtResponse(String accessToken, String refreshToken, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;

    }

}
