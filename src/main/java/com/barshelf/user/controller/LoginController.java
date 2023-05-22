package com.barshelf.user.controller;

import com.barshelf.user.entities.BarshelfUser;
import com.barshelf.user.repository.BarshelfUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    BarshelfUserRepository barshelfUserRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid BarshelfUser user) {

        ResponseEntity<String> response = null;
        try {
            BarshelfUser savedUser = barshelfUserRepository.save(user);
            if (savedUser.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to" + ex.getMessage());
        }
        return response;
    }
}
