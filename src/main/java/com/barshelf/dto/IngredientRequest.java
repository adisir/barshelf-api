package com.barshelf.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredientRequest {
    @NotBlank
    private String name;
}
