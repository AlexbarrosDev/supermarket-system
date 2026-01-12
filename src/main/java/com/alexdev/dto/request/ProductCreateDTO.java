package com.alexdev.dto.request;

import com.alexdev.entities.Category;
import com.alexdev.entities.Mark;

public record ProductCreateDTO(String name,
                               Double price,
                               Category category,
                               Mark mark) {
}
