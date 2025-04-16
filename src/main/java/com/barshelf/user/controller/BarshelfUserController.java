package com.barshelf.user.controller;

import com.barshelf.user.entities.BarshelfUser;
import com.barshelf.user.repository.BarshelfUserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class BarshelfUserController {
    BarshelfUserRepository barshelfUserRepository;
    private final PasswordEncoder passwordEncoder;

    //Register users to BarShelf
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid BarshelfUser user) {

        ResponseEntity<String> response = null;
        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPwd);
            BarshelfUser savedUser = barshelfUserRepository.save(user);
            if (savedUser.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("User details are successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred:" + ex.getMessage());
        }
        return response;
    }

    @RequestMapping("/user")
    public BarshelfUser getUserDetails(Authentication authentication) {
        Optional<BarshelfUser> optionalBarshelfUser = barshelfUserRepository.findByEmail(authentication.getName());
        return optionalBarshelfUser.orElse(null);
    }
    @GetMapping("/demo")
    public String demo() {
        return "Yipee";
    }
}
