
package com.barshelf.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.ROLE_USER;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }
    // Simple barshelf as collection of ingredient names
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_ingredients", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ingredient_name")
    private Set<String> ingredients;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Ingredient management helper methods
    public void addIngredient(String ingredientName) {
        if (ingredientName != null && !ingredientName.trim().isEmpty()) {
            this.ingredients.add(ingredientName.trim());
        }
    }

    public void removeIngredient(String ingredientName) {
        if (ingredientName != null) {
            this.ingredients.remove(ingredientName.trim());
        }
    }

    public boolean hasIngredient(String ingredientName) {
        return this.ingredients.contains(ingredientName.trim());
    }
}