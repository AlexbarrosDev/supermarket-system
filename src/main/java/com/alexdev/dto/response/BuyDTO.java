package com.alexdev.dto.response;

import java.time.Instant;
import java.util.List;

public record BuyDTO(Long id,
                     Instant date,
                     ClientDTO clientDTO,
                     List<BuyItemDTO> buyItemDTOList,
                     Double total) {
}
