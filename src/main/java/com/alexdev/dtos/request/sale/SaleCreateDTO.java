package com.alexdev.dtos.request.sale;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleCreateDTO(

        @NotNull(message = "Mandatory clientId!")
        Long clientId,

        @NotEmpty(message = "Add the products sold!")
        List<ItemSoldCreateDTO> items) {
}
