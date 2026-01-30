package com.example.Nansy_server.service;

import org.springframework.stereotype.Service;
import com.example.Nansy_server.model.UserModel;

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

    public UserModel createTestUser() {
        UserModel user = new UserModel();
        user.setUsername("Ольга");

        return userRepository.save(user);
    }

    public UserModel getOrCreateUser(String username) {
        Optional<UserModel> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) { 
            System.out.println("Пользователь найден");
            return existingUser.get();
        }

        UserModel newUser = new UserModel();
        newUser.setUsername(username);

        return userRepository.save(newUser);
    }
}
