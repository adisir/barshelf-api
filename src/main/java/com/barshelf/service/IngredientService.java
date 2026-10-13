package com.barshelf.service;

import com.barshelf.entity.User;
import com.barshelf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {
    private final UserRepository userRepository;
    public List<String> getUserIngredients(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ArrayList<>(user.getIngredients());
    }
    public void addIngredient(Long userId, String ingredientName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.addIngredient(ingredientName);
        userRepository.save(user);
    }
    public void deleteIngredient(Long userId, String ingredientName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if(user.hasIngredient(ingredientName)) {
            user.removeIngredient(ingredientName);
            userRepository.save(user);
        }
    }
}
