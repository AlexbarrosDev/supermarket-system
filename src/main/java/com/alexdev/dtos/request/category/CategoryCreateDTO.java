package com.alexdev.dtos.request.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(

        @NotBlank(message = "Mandatory Category name!")
        String name
) {
}
