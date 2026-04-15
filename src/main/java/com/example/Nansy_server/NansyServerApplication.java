package com.example.Nansy_server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.Nansy_server.service.UserService;

@SpringBootApplication
public class NansyServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NansyServerApplication.class, args);
	}

	@Bean
    public CommandLineRunner testUserService(UserService userService) {
        return args -> {
            // Здесь можно вызывать методы
            userService.getOrCreateUser("Ольга");
            System.out.println("✅ Тестовый пользователь создан!");
        };
    }
}