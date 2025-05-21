package com.psi2.donaciones.controller;

import com.psi2.donaciones.auth.AuthRequest;
import com.psi2.donaciones.auth.AuthResponse;
import com.psi2.donaciones.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("Intento de login recibido para CI: " + request.getCedulaIdentidad());

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
