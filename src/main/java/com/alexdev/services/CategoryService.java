package com.alexdev.services;

import com.alexdev.dto.request.CategoryCreateDTO;
import com.alexdev.dto.response.CategoryDTO;
import com.alexdev.entities.Category;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.CategoryMapper;
import com.alexdev.repositories.CategoryRepository;
import com.alexdev.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(CategoryMapper::entityToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return CategoryMapper.entityToDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (productRepository.existsByCategoryId(id)) {
            throw new BusinessException(
                    "Cannot delete category: it is being used by product");
        }
        repository.deleteById(id);
    }

    @Transactional
    public CategoryDTO create(CategoryCreateDTO categoryCreateDTO) {
        if (categoryCreateDTO.name() == null || categoryCreateDTO.name().isBlank()) {
            throw new BusinessException("Category name must not be null or blank");
        }
        Category entity = new Category(categoryCreateDTO.name());
        return CategoryMapper.entityToDTO(repository.save(entity));
    }
}
