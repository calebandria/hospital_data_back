package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Identification;
import com.example.demo.model.Users;
import com.example.demo.repository.IdentificationRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Getter;

@Service
@Getter
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder, IdentificationRepository identificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.identificationRepository = identificationRepository;
    }

    @Transactional
    public Users registerUser(String username, String password, long identification) {
        Users user = new Users();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        Identification identificationFound = identificationRepository.findByIdentification(identification)
            .orElseThrow(() -> new RuntimeException("Identification not found "));
        
        user.setUsername(username);
        user.setRole(identificationFound.getRole());
        user.setPassword(passwordEncoder.encode(password));
        user.setIdentification(identificationFound);
        identificationFound.setUser(user);

        return userRepository.save(user);
    }

}
