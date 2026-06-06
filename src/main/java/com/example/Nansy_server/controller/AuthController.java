package com.example.Nansy_server.controller;

import com.example.Nansy_server.service.UserService;  
import com.example.Nansy_server.util.JwtUtil;        
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController  
@RequestMapping("/auth")  
public class AuthController {
    
    @Autowired  
    private JwtUtil jwtUtil;
    
    @Autowired  
    private UserService userService;
    
    
    @PostMapping("/login")  
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        boolean loginStatus = userService.loginUser(request.getUsername(), request.getPassword());
        
        if (loginStatus == true) {
            return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(request.getUsername()), request.getUsername()));
        }
        
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        boolean registerStatus = userService.registerUser(request.getUsername(), request.getPassword());

        if (registerStatus == true) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }
    
    static class LoginRequest {
        private String username;  
        private String password;
        
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getUsername() {
            return username; 
        }
        public void setUsername(String username) { 
            this.username = username; 
        }
    }
    
    static class LoginResponse {
        private String token;
        private String username;
        
        public LoginResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
        
        public String getToken() { return token; }
        public String getUsername() { return username; }
    }
}