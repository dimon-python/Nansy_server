package com.example.Nansy_server.controller;

import com.example.Nansy_server.service.UserService;  
import com.example.Nansy_server.util.JwtUtil;        
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.*;

@RestController  
@RequestMapping("/auth")  
public class AuthController {
    
    @Autowired  
    private JwtUtil jwtUtil;
    
    @Autowired  
    private UserService userService;
    
    
    @PostMapping("/login")  
    public AuthResponse login(@RequestBody LoginRequest request) {
        
        userService.getOrCreateUser(request.getUsername());
        
        
        String token = jwtUtil.generateToken(request.getUsername());
        
        System.out.println("🔐 User logged in: " + request.getUsername() + ", token generated");
        
        
        return new AuthResponse(token, request.getUsername());
    }
    

    static class LoginRequest {
        private String username;  
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
    
    static class AuthResponse {
        private String token;      
        private String username;
        
        public AuthResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
        
        public String getToken() { return token; }
        public String getUsername() { return username; }
    }
}