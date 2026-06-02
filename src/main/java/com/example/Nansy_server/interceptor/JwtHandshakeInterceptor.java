package com.example.Nansy_server.interceptor;

import com.example.Nansy_server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest; 
import org.springframework.http.server.ServerHttpResponse;  
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;    
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map; 

@Component 
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    
    @Autowired 
    private JwtUtil jwtUtil;
    
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, 
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler, 
                                   Map<String, Object> attributes) {
            
        String token = extractToken(request);
        
        // 2. Проверяем: токен существует и валиден
        if (token != null && jwtUtil.validateToken(token)) {
            // 3. Извлекаем username из токена
            String username = jwtUtil.extractUsername(token);
            
            // 4. Сохраняем username в атрибуты WebSocket сессии
            // Эти атрибуты будут доступны в контроллере через Principal
            attributes.put("username", username);
            
            // 5. Можно добавить дополнительные данные
            attributes.put("authenticated", true);
            attributes.put("token", token);
            
            System.out.println("✅ WebSocket handshake SUCCESS for user: " + username);
            
            // 6. Разрешаем подключение (true = продолжаем handshake)
            return true;
        }
        
        // 7. Токен невалиден или отсутствует - отклоняем подключение
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        System.out.println("❌ WebSocket handshake REJECTED: invalid or missing JWT");
        return false;
    }
    
    // Приватный метод: извлекает JWT токен из запроса (из заголовка или URL параметра)
    private String extractToken(ServerHttpRequest request) {
        // Пытаемся получить из заголовка Authorization
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Убираем "Bearer "
        }
        
        // Если нет в заголовке - пробуем из URL параметра (например, ?token=...)
        if (request instanceof ServletServerHttpRequest) {
            String token = ((ServletServerHttpRequest) request)
                    .getServletRequest()
                    .getParameter("token");
            if (token != null && !token.isEmpty()) {
                return token;
            }
        }
        
        return null;  // Токен не найден
    }
    
    // Вызывается ПОСЛЕ установки WebSocket соединения (для пост-обработки)
    @Override
    public void afterHandshake(ServerHttpRequest request,         // HTTP запрос
                               ServerHttpResponse response,       // HTTP ответ
                               WebSocketHandler wsHandler,        // Обработчик WebSocket
                               Exception exception) {             // Ошибка (если была)
        
        // Можно добавить логирование успешных подключений
        if (exception == null) {
            System.out.println("🔌 WebSocket connection established successfully");
        } else {
            System.out.println("⚠️ WebSocket connection error: " + exception.getMessage());
        }
    }
}