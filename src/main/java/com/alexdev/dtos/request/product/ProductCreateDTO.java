package com.alexdev.dtos.request.product;

import com.alexdev.domain.product.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductCreateDTO(

        @NotBlank(message = "Mandatory name!")
        String name,

        @NotNull(message = "Mandatory currentPrice!")
        BigDecimal currentPrice,

        @NotNull(message = "Mandatory categoryId!")
        Long categoryId,

        @NotNull(message = "Mandatory groupId!")
        Long groupId,

        @NotNull(message = "Mandatory productStatus!")
        ProductStatus status
) {}