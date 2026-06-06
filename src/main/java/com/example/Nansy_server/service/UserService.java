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
    return userRepository.findByUsername(username)
        .map(user -> encoder.matches(password, user.getPassword()))
        .orElse(false);
    }

    private UserModel createUser(String username, String password) {
        UserModel user = new UserModel();
        user.setUsername(username);
        String hashPassword = encoder.encode(password);
        user.setPassword(hashPassword);
        return userRepository.save(user);
    }
    
    public boolean registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        } else {
            createUser(username, password);
            return true;
        }
    }
}
