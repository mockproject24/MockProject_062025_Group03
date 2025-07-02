package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String fullname;
    private String role;
    
    public JwtResponse(String token, String username, String email, String fullname, String role) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
    }
} 