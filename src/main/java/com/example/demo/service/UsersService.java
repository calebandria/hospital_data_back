package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Getter;

@Service
@Getter
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Users registerUser(String username, String password, Role role) {
        Users user = new Users();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        user.setUsername(username);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

}
