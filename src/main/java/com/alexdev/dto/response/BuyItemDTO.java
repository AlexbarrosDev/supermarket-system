package com.alexdev.dto.response;

public record BuyItemDTO(Long id,
                         ProductDTO productDTO,
                         Integer quantity,
                         Double unitPrice,
                         Double subTotal) {
}
