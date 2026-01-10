package com.streamgambia.auth.controller;

import com.streamgambia.auth.dto.LoginRequest;
import com.streamgambia.auth.dto.RegisterRequest;
import com.streamgambia.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
// @CrossOrigin is removed because we handle it in SecurityConfig now
public class AuthController {

    private final AuthService authService;

    // --- THIS WAS MISSING ---
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // ------------------------

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}