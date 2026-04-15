package com.example.Nansy_server.controller;

import com.example.Nansy_server.service.UserService;  // Сервис для работы с пользователями
import com.example.Nansy_server.util.JwtUtil;        // Утилита для генерации JWT
import org.springframework.beans.factory.annotation.Autowired; // Внедрение зависимостей
import org.springframework.web.bind.annotation.*;    // Аннотации для REST контроллера

@RestController  // Контроллер для REST API (возвращает JSON)
@RequestMapping("/auth")  // Все методы будут доступны по URL: /auth/...
public class AuthController {
    
    @Autowired  // Внедряем JwtUtil для генерации токенов
    private JwtUtil jwtUtil;
    
    @Autowired  // Внедряем UserService для работы с пользователями в БД
    private UserService userService;
    
    // Эндпоинт: POST /auth/login
    @PostMapping("/login")  // HTTP метод POST
    public AuthResponse login(@RequestBody LoginRequest request) {
        // 1. Создаём или получаем пользователя из БД по username
        userService.getOrCreateUser(request.getUsername());
        
        // 2. Генерируем JWT токен для этого пользователя
        String token = jwtUtil.generateToken(request.getUsername());
        
        System.out.println("🔐 User logged in: " + request.getUsername() + ", token generated");
        
        // 3. Возвращаем ответ с токеном и username
        return new AuthResponse(token, request.getUsername());
    }
    
    // Внутренний класс для тела запроса (JSON, который присылает клиент)
    static class LoginRequest {
        private String username;  // Поле "username" в JSON
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
    
    // Внутренний класс для тела ответа (JSON, который получит клиент)
    static class AuthResponse {
        private String token;      // JWT токен
        private String username;   // Имя пользователя
        
        public AuthResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
        
        public String getToken() { return token; }
        public String getUsername() { return username; }
    }
}