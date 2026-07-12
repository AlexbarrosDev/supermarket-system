package com.alexdev.exceptions.handler;

import java.time.Instant;
import java.util.Map;

public record ErrorResponseDTO(
        Instant timestamp,
        Integer status,
        String error,
        Map<String, String> fields) {
}
