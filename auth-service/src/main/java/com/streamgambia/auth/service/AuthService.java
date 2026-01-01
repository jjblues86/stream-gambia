package com.streamgambia.auth.service;

import com.streamgambia.auth.dto.AuthResponse;
import com.streamgambia.auth.dto.LoginRequest;
import com.streamgambia.auth.dto.RegisterRequest;
import com.streamgambia.auth.entity.Role;
import com.streamgambia.auth.entity.User;
import com.streamgambia.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        // 1. Validation
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists!");
        }

        // 2. Create User Entity
        User user = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Hash the password
                .role(Role.USER)
                .active(true)
                .build();

        // 3. Save to DB
        userRepository.save(user);

        return "User registered successfully!";
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Find User
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Check Password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // 3. Generate Token
        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
