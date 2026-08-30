package com.alexdev.services;

import com.alexdev.domain.category.Category;
import com.alexdev.dtos.request.category.CategoryCreateDTO;
import com.alexdev.dtos.request.category.CategoryUpdateDTO;
import com.alexdev.dtos.response.category.CategoryDetailsDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.CategoryMapper;
import com.alexdev.repositories.CategoryRepository;
import com.alexdev.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryDetailsDTO> findAllCategories() {

        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }

        return categoryMapper
                .categoryEntityListToCategoryDetailsDTOList(categoryList);
    }

    @Transactional(readOnly = true)
    public CategoryDetailsDTO findCategoryById(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()
                    -> new ResourceNotFoundException("Category not found"));

        return categoryMapper.categoryEntityToCategoryDetailsDTO(category);
    }

    @Transactional
    public CategoryDetailsDTO createCategory(CategoryCreateDTO categoryUpdateDTO) {

        validateCreateNameUniqueness(categoryUpdateDTO.name());

        Category entity = categoryMapper
                .categoryCreateDTOToCategoryEntity(categoryUpdateDTO);

        entity = categoryRepository.save(entity);

        return categoryMapper
                .categoryEntityToCategoryDetailsDTO(entity);
    }

    @Transactional
    public CategoryDetailsDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Category not found" + id));

        if (categoryRepository.existsByNameAndIdNot(categoryUpdateDTO.name(), id)) {
            throw new BusinessException("Category already exists");
        }

        category.setName(categoryUpdateDTO.name());

        return categoryMapper
                .categoryEntityToCategoryDetailsDTO(category);
    }

    @Transactional
    public void deleteCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (productRepository.existsByCategoryId(id)) {
            throw new BusinessException(
                    "Category cannot be deleted because it is associated with one or more products."
            );
        }
        categoryRepository.delete(category);
    }

    private void validateCreateNameUniqueness(String name) {

        if (categoryRepository.existsByName(name)) {
            throw new BusinessException("Category already exists.");
        }
    }
}
