package com.barshelf.controller;
import com.barshelf.dto.JwtResponse;
import com.barshelf.dto.LoginRequest;
import com.barshelf.dto.RegisterRequest;
import com.barshelf.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        JwtResponse jwtResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
