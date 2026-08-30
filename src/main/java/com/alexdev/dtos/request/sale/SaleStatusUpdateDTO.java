package com.alexdev.dtos.request.sale;

import com.alexdev.domain.sale.SaleStatus;
import jakarta.validation.constraints.NotNull;

public record SaleStatusUpdateDTO(

        @NotNull(message = "Mandatory status!")
        SaleStatus status
){}
