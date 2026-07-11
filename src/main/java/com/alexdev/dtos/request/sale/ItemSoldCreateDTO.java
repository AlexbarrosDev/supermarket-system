package com.alexdev.dtos.request.sale;

import jakarta.validation.constraints.NotNull;

public record ItemSoldCreateDTO(

        @NotNull(message = "Mandatory product!")
        Long productId,

        @NotNull(message = "Invalid quantity!")
        Integer quantity
) {
}
