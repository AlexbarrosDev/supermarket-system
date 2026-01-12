package com.alexdev.mappers;

import com.alexdev.dto.response.CategoryDTO;
import com.alexdev.entities.Category;

public class CategoryMapper {

    public static CategoryDTO entityToDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(category.getId(), category.getName());
    }
}
