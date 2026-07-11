package com.alexdev.dtos.response.sale;

import com.alexdev.dtos.response.product.ProductSummaryDTO;

import java.math.BigDecimal;

public record ItemSoldDetailsDTO(

        ProductSummaryDTO product,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {}
