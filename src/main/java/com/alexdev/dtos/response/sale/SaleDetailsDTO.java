package com.alexdev.dtos.response.sale;

import com.alexdev.domain.sale.SaleStatus;
import com.alexdev.dtos.response.client.ClientSummaryDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record SaleDetailsDTO(

        Long id,
        Instant saleDate,
        SaleStatus status,
        ClientSummaryDTO client,
        List<ItemSoldDetailsDTO> items,
        BigDecimal total) {
}
