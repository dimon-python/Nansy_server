package com.example.Nansy_server.config;

import com.example.Nansy_server.interceptor.JwtHandshakeInterceptor; // Наш JWT перехватчик
import org.springframework.beans.factory.annotation.Autowired;        // Внедрение зависимостей
import org.springframework.context.annotation.Configuration;         // Помечаем как конфигурацию
import org.springframework.messaging.simp.config.MessageBrokerRegistry; // Настройка брокера сообщений
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker; // Включаем STOMP
import org.springframework.web.socket.config.annotation.StompEndpointRegistry; // Регистрация STOMP эндпоинтов
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer; // Интерфейс конфигурации

@Configuration  // Говорим Spring: "Этот класс содержит настройки"
@EnableWebSocketMessageBroker  // Включаем WebSocket с поддержкой STOMP (брокер сообщений)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Autowired  // Внедряем наш перехватчик для проверки JWT
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;
    
    // Настройка брокера сообщений (куда и как отправлять сообщения)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Включаем простой брокер для префиксов:
        // /topic - для публичных чатов/комнат
        // /queue - для личных очередей (point-to-point)
        // /user - для сообщений конкретному пользователю
        config.enableSimpleBroker("/topic", "/queue", "/user");
        
        // Префикс для методов, обрабатывающих входящие сообщения (@MessageMapping)
        // Клиент будет отправлять на /app/requestToPC
        config.setApplicationDestinationPrefixes("/app");
        
        // Префикс для личных сообщений (Spring автоматически преобразует /user/{user}/queue в /queue/{user})
        config.setUserDestinationPrefix("/user");
    }
    
    // Регистрация STOMP эндпоинта (точки подключения для WebSocket)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Добавляем эндпоинт /ws - клиенты будут подключаться сюда
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Разрешаем подключения с любых источников
                .addInterceptors(jwtHandshakeInterceptor);  // ← ДОБАВЛЯЕМ JWT ПРОВЕРКУ!
    }
}