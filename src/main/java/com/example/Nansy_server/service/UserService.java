package com.example.Nansy_server.service;

import org.springframework.stereotype.Service;
import com.example.Nansy_server.model.UserModel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Nansy_server.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean loginUser(String username, String password) {
        Optional<UserModel> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            UserModel user = existingUser.get();
            String savedHash = user.getPassword();
            return encoder.matches(password, savedHash);
        } else {
            return false;
        }
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
