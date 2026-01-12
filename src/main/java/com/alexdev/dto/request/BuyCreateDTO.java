package com.alexdev.dto.request;

import java.time.Instant;
import java.util.List;

public record BuyCreateDTO(Instant date, Long ClientId, List<BuyItemCreateDTO> BuyItems) {
}
