package com.example.Nansy_server.service;

import com.example.Nansy_server.model.UserModel;          // Наша сущность пользователя
import com.example.Nansy_server.repository.UserRepository; // Репозиторий для работы с БД
import org.springframework.beans.factory.annotation.Autowired; // Внедрение зависимостей
import org.springframework.security.core.userdetails.User;     // Spring Security пользователь
import org.springframework.security.core.userdetails.UserDetails; // Интерфейс пользователя
import org.springframework.security.core.userdetails.UserDetailsService; // Сервис загрузки
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Исключение
import org.springframework.stereotype.Service; // Регистрация как Spring сервис

@Service  // Регистрируем как Spring сервис (бизнес-логика)
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired  // Внедряем репозиторий для работы с пользователями в БД
    private UserRepository userRepository;
    
    // Метод, который Spring Security вызывает для загрузки пользователя по username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя в БД по username
        UserModel userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Преобразуем нашу UserModel в объект, который понимает Spring Security
        // User.builder() - строитель Spring Security User
        return User.builder()
                .username(userModel.getUsername())  // Логин
                .password("")                       // Пароль пустой, т.к. используем JWT
                .authorities("USER")                // Роль/права (можно расширить)
                .build();
    }
}