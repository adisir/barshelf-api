package com.barshelf.controller;

import com.barshelf.dto.IngredientRequest;
import com.barshelf.service.IngredientService;
import com.barshelf.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<String>> getUserIngredients(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<String> ingredients = ingredientService.getUserIngredients(userPrincipal.getId());
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping
    public ResponseEntity<String> addIngredient(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody IngredientRequest request) {
        ingredientService.addIngredient(userPrincipal.getId(), request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient added to your collection!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteIngredient(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody IngredientRequest request) {
        ingredientService.deleteIngredient(userPrincipal.getId(), request.getName());
        return ResponseEntity.noContent().build();
    }
}
