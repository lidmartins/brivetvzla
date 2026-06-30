package com.brivetvzla.backend.controller;

import com.brivetvzla.backend.model.dto.request.LoginRequest;
import com.brivetvzla.backend.service.UserService;
import com.brivetvzla.backend.service.UserService.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /auth/login
     * Body: { "email": "vet@vpv.com", "password": "demo1234" }
     * Response: { "usuario": { id, nombre, apellido, email, ... }, "token": "eyJ..." }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
