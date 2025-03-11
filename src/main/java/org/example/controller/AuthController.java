package org.example.controller;

import org.example.payload.request.CustomerRegistrationRequest;
import org.example.payload.request.LoginRequest;
import org.example.payload.request.ServiceProviderRegistrationRequest;
import org.example.payload.response.JwtResponse;
import org.example.payload.response.MessageResponse;
import org.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest registrationRequest) {
        MessageResponse response = authService.registerCustomer(registrationRequest);

        if (response.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/service-provider")
    public ResponseEntity<?> registerServiceProvider(@Valid @RequestBody ServiceProviderRegistrationRequest registrationRequest) {
        MessageResponse response = authService.registerServiceProvider(registrationRequest);

        if (response.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}