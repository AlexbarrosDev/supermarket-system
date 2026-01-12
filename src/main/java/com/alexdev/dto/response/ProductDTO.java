package com.alexdev.dto.response;

public record ProductDTO(Long id,
                         String name,
                         Double price,
                         CategoryDTO categoryDTO,
                         MarkDTO markDTO) {
}
