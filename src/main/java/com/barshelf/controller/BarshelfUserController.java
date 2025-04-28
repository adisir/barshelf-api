package com.barshelf.controller;

import com.barshelf.entities.BarshelfUser;
import com.barshelf.repository.BarshelfUserRepository;
import com.barshelf.service.BarshelfUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
public class BarshelfUserController {
    BarshelfUserRepository barshelfUserRepository;
    private final PasswordEncoder passwordEncoder;
    BarshelfUserService barshelfUserService;

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

    @PostMapping("/addToBarshelf")
    public ResponseEntity<String> addIngredientToBarshelf(@RequestParam String ingredientName, Authentication authentication) {
        String email = authentication.getName(); // Get authenticated user's email
        String result = barshelfUserService.addIngredientToUserBarshelf(email, ingredientName);
        if ("Ingredient added successfully".equals(result)) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else if ("User not found".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if ("Ingredient already exists in the barshelf".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping ("/getBarshelf")
    public ResponseEntity<Set<String>> getBarshelf(Authentication authentication) {
        String email = authentication.getName(); // Get authenticated user's email
        Set<String> barshelf = barshelfUserService.getUserBarshelf(email);
        return ResponseEntity.ok(barshelf);
    }
    @GetMapping("/demo")
    public String demo() {
        return "Yipee";
    }
}
