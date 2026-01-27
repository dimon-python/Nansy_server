package com.example.Nansy_server.service;

import org.springframework.stereotype.Service;
import com.example.Nansy_server.model.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.Nansy_server.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createTestUser() {
        User user = new User();
        user.setUsername("Ольга");

        return userRepository.save(user);
    }

    public User getOrCreateUser(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) { 
            System.out.println("Пользователь найден");
            return existingUser.get();
        }

        User newUser = new User();
        newUser.setUsername(username);

        return userRepository.save(newUser);
    }
}
