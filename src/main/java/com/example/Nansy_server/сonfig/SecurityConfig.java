package com.example.Nansy_server.config;

import org.springframework.context.annotation.Bean;               // Для создания бинов
import org.springframework.context.annotation.Configuration;      // Помечаем как конфигурационный класс
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Строитель конфигурации
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Включаем Security
import org.springframework.security.config.http.SessionCreationPolicy; // Политика создания сессий
import org.springframework.security.web.SecurityFilterChain;     // Цепочка фильтров безопасности
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Стандартный фильтр
import com.example.Nansy_server.filter.JwtAuthFilter;           // Наш JWT фильтр

@Configuration        // Говорим Spring: "Этот класс содержит настройки"
@EnableWebSecurity    // Включаем Spring Security (без этой аннотации Security не работает)
public class SecurityConfig {
    
    @Bean   // Регистрируем объект типа SecurityFilterChain в Spring контейнере
    public SecurityFilterChain filterChain(HttpSecurity http,           // Конфигуратор безопасности (внедряется Spring)
                                           JwtAuthFilter jwtAuthFilter) // Наш JWT фильтр (внедряется Spring)
            throws Exception {
        
        http
            // 1. Отключаем CSRF защиту (не нужна для JWT, т.к. не используем cookies)
            .csrf(csrf -> csrf.disable())
            
            // 2. Настраиваем управление сессиями
            .sessionManagement(session -> session
                // STATELESS - НЕ создаём HTTP сессии (каждый запрос независим)
                // Это стандарт для JWT аутентификации
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. Настраиваем какие URL доступны без аутентификации
            .authorizeHttpRequests(auth -> auth
                // Разрешаем доступ к WebSocket handshake без JWT
                // (фактическая проверка JWT будет в JwtHandshakeInterceptor)
                .requestMatchers("/ws/**").permitAll()
                
                // Разрешаем доступ к эндпоинту /auth/** (логин, регистрация)
                .requestMatchers("/auth/**").permitAll()
                
                // ВСЕ остальные URL требуют аутентификации (наличия валидного JWT)
                .anyRequest().authenticated()
            )
            
            // 4. Добавляем наш JWT фильтр ПЕРЕД стандартным фильтром аутентификации
            // Порядок: сначала JwtAuthFilter, потом UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 5. Строим и возвращаем цепочку фильтров
        return http.build();
    }
}