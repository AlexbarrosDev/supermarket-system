package com.alexdev.dtos.response.product;

import com.alexdev.domain.enums.ProductStatus;
import com.alexdev.dtos.response.category.CategoryDetailsDTO;
import com.alexdev.dtos.response.group.GroupDetailsDTO;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductDetailsDTO(
        Long id,
        String name,
        BigDecimal currentPrice,
        CategoryDetailsDTO category,
        GroupDetailsDTO group,
        ProductStatus status,
        Instant createdAt
) {}
