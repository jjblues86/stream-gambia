package com.streamgambia.auth.service;

import com.streamgambia.auth.dto.LoginRequest;
import com.streamgambia.auth.dto.RegisterRequest;
import com.streamgambia.auth.entity.User;
import com.streamgambia.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Assuming you have this

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {
        // OLD: User.builder()...build()
        // NEW: Constructor
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber()
        );

        userRepository.save(user);
        return jwtService.generateToken(user.getEmail());
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}