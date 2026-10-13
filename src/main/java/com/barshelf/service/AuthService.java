package com.barshelf.service;

import com.barshelf.dto.JwtResponse;
import com.barshelf.dto.RegisterRequest;
import com.barshelf.entity.User;
import com.barshelf.repository.UserRepository;
import com.barshelf.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public JwtResponse authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateJwtToken(authentication);

            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    role);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {} - Error: {}", username, e.getMessage());
            throw e;
        }
    }

    public JwtResponse registerUser(@Valid RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return authenticateUser(signUpRequest.getUsername(), signUpRequest.getPassword());
    }
}
