package com.alexdev.mappers;

import com.alexdev.dtos.request.category.CategoryCreateDTO;
import com.alexdev.dtos.response.category.CategoryDetailsDTO;
import com.alexdev.domain.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category categoryCreateDTOToCategoryEntity(CategoryCreateDTO categoryCreateDTO);

    CategoryDetailsDTO categoryEntityToCategoryDetailsDTO(Category category);

    List<Category> categoryCreateDTOListToCategoryEntityList(List<CategoryCreateDTO> categories);

    List<CategoryDetailsDTO> categoryEntityListToCategoryDetailsDTOList(List<Category> categories);
}
