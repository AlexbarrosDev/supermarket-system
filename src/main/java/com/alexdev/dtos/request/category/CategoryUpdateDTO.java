package com.alexdev.dtos.request.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateDTO(

        @NotBlank(message = "Mandatory Category name!")
        String name
) {
}
