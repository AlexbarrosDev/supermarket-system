package com.alexdev.dtos.request.product;

import com.alexdev.domain.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateDTO(

        @NotNull(message = "Mandatory status!")
        ProductStatus status
) {}
