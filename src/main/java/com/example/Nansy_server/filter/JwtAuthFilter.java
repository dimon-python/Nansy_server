package com.example.Nansy_server.filter;

import jakarta.servlet.FilterChain;                    // Цепочка фильтров Servlet
import jakarta.servlet.ServletException;               // Исключения сервлета
import jakarta.servlet.http.HttpServletRequest;        // Входящий HTTP запрос
import jakarta.servlet.http.HttpServletResponse;       // Исходящий HTTP ответ
import org.springframework.beans.factory.annotation.Autowired; // Внедрение зависимостей
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Объект аутентификации
import org.springframework.security.core.context.SecurityContextHolder; // Хранилище текущего пользователя
import org.springframework.security.core.userdetails.UserDetails;       // Интерфейс пользователя
import org.springframework.security.core.userdetails.UserDetailsService; // Сервис загрузки пользователя
import org.springframework.stereotype.Component;       // Регистрация как Spring Bean
import org.springframework.web.filter.OncePerRequestFilter; // Базовый класс: фильтр выполняется 1 раз на запрос

import com.example.Nansy_server.util.JwtUtil;          // Наш утилитный класс для JWT

import java.io.IOException;                            // Ошибки ввода-вывода

@Component  // Регистрируем как Spring компонент (будет автоматически найден и подключён)
public class JwtAuthFilter extends OncePerRequestFilter {
    
    @Autowired  // Внедряем JwtUtil для работы с токенами
    private JwtUtil jwtUtil;
    
    @Autowired  // Внедряем сервис для загрузки пользователя из БД
    private UserDetailsService userDetailsService;
    
    // Главный метод фильтра: вызывается для каждого HTTP запроса
    @Override
    protected void doFilterInternal(HttpServletRequest request,   // Входящий запрос
                                    HttpServletResponse response, // Исходящий ответ
                                    FilterChain filterChain)      // Цепочка следующих фильтров
            throws ServletException, IOException {
        
        // 1. Получаем заголовок Authorization из HTTP запроса
        String authHeader = request.getHeader("Authorization");
        
        // 2. Проверяем: заголовок существует и начинается с "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3. Вырезаем сам токен (убираем "Bearer " в начале)
            String token = authHeader.substring(7);  // 7 - длина "Bearer "
            
            // 4. Валидируем токен (проверяем подпись и срок действия)
            if (jwtUtil.validateToken(token)) {
                // 5. Извлекаем username из токена
                String username = jwtUtil.extractUsername(token);
                
                // 6. Загружаем пользователя из БД (через UserDetailsService)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 7. Создаём объект аутентификации (Spring Security понятие "залогинен")
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,    // Принципал (кто залогинен)
                                null,           // Учётные данные (пароль) - не нужны
                                userDetails.getAuthorities()  // Роли/права пользователя
                        );
                
                // 8. Кладём объект аутентификации в SecurityContext (Spring Security теперь знает о пользователе)
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                System.out.println("✅ User authenticated via JWT: " + username);
            } else {
                System.out.println("❌ Invalid JWT token");
            }
        } else {
            System.out.println("No Authorization header found");
        }
        
        // 9. Передаём запрос дальше по цепочке фильтров (к контроллеру)
        filterChain.doFilter(request, response);
    }
}