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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() {

        // Arrange
        Category category = createCategory("Category");
        List<Category> categories = List.of(category);

        CategoryDetailsDTO dto = new CategoryDetailsDTO(1L, "Category");
        List<CategoryDetailsDTO> dtos = List.of(dto);

        when(categoryRepository.findAll())
                .thenReturn(categories);

        when(categoryMapper
                .categoryEntityListToCategoryDetailsDTOList(categories))
                .thenReturn(dtos);

        // Act
        List<CategoryDetailsDTO> result = categoryService.findAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getFirst().id());
        assertEquals("Category", result.getFirst().name());

        verify(categoryRepository).findAll();
        verify(categoryMapper)
                .categoryEntityListToCategoryDetailsDTOList(categories);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCategoryListIsEmpty() {

        List<Category> categories = List.of();

        when(categoryRepository.findAll()).thenReturn(categories);

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.findAllCategories());

        verify(categoryRepository).findAll();
        verify(categoryMapper, never())
                .categoryEntityListToCategoryDetailsDTOList(any());
    }

    @Test
    void shouldReturnCategoryById() {

        // Arrange
        Category category = createCategory("Category");

        CategoryDetailsDTO dto = new CategoryDetailsDTO(1L, "Category");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryMapper
                .categoryEntityToCategoryDetailsDTO(category))
                .thenReturn(dto);

        // Act
        CategoryDetailsDTO result = categoryService.findCategoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Category", result.name());

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).categoryEntityToCategoryDetailsDTO(category);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCategoryIsNotFound() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () ->  categoryService.findCategoryById(1L));

        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never())
                .categoryEntityToCategoryDetailsDTO(any());
    }

    @Test
    void shouldCreateCategory() {

        // Arrange
        CategoryCreateDTO dto = new CategoryCreateDTO("Category");

        Category category = createCategory("Category");

        CategoryDetailsDTO dtoDetails = new CategoryDetailsDTO(1L, "Category");

        when(categoryRepository.existsByName("Category"))
                .thenReturn(false);

        when(categoryMapper.categoryCreateDTOToCategoryEntity(dto)).thenReturn(category);

        when(categoryRepository.save(category)).thenReturn(category);

        when(categoryMapper
                .categoryEntityToCategoryDetailsDTO(category)).thenReturn(dtoDetails);

        // Act
        CategoryDetailsDTO result = categoryService.createCategory(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Category", result.name());

        verify(categoryRepository).existsByName("Category");
        verify(categoryMapper)
                .categoryCreateDTOToCategoryEntity(dto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper).categoryEntityToCategoryDetailsDTO(category);
    }

    @Test
    void shouldThrowBusinessExceptionWhenCategoryNameAlreadyExistsOnCreate() {

        CategoryCreateDTO dto = new CategoryCreateDTO("Category");

        when(categoryRepository.existsByName("Category")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> categoryService.createCategory(dto));

        verify( categoryRepository).existsByName("Category");
        verify(categoryMapper, never()).categoryCreateDTOToCategoryEntity(dto);
        verify(categoryMapper, never()).categoryEntityToCategoryDetailsDTO(any());
    }

    @Test
    void shouldUpdateCategory() {

        // Arrange
        CategoryUpdateDTO dto = new CategoryUpdateDTO("Category2");

        Category category = createCategory("Category");

        CategoryDetailsDTO dtoDetails = new CategoryDetailsDTO(1L, "Category2");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndIdNot(dto.name(), 1L)).thenReturn(false);
        when(categoryMapper.categoryEntityToCategoryDetailsDTO(category))
                .thenReturn(dtoDetails);

        // Act
        CategoryDetailsDTO result = categoryService.updateCategory(1L, dto);

        // Assert
        assertEquals("Category2", category.getName());
        assertEquals("Category2", result.name());
        assertEquals(1L, result.id());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).existsByNameAndIdNot(dto.name(), 1L);
        verify(categoryMapper).categoryEntityToCategoryDetailsDTO(category);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingCategory() {

        CategoryUpdateDTO dto = new CategoryUpdateDTO("Category2");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(1L, dto));

        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).existsByNameAndIdNot(anyString(), anyLong());
        verify(categoryMapper, never()).categoryEntityToCategoryDetailsDTO(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenCategoryNameAlreadyExists() {

        CategoryUpdateDTO dto = new CategoryUpdateDTO("Category");

        Category category = createCategory("Category2");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndIdNot(dto.name(), 1L)).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> categoryService.updateCategory(1L ,dto));

        assertEquals("Category2", category.getName());

        verify(categoryRepository).existsByNameAndIdNot(dto.name(), 1L);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never()).categoryEntityToCategoryDetailsDTO(any());
    }

    @Test
    void shouldDeleteCategoryById() {

        // Arrange
        Category category = createCategory("Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsByCategoryId(1L)).thenReturn(false);

        // Act
        categoryService.deleteCategoryById(1L);

        // Assert
        verify(categoryRepository).findById(1L);
        verify(productRepository).existsByCategoryId(1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldThrowBusinessExceptionWhenCategoryContainsProducts() {

        Category category = createCategory("Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.existsByCategoryId(1L)).thenReturn(true);

        assertThrows(BusinessException.class,
                () ->  categoryService.deleteCategoryById(1L));

        verify(categoryRepository).findById(1L);
        verify(productRepository).existsByCategoryId(1L);
        verify(categoryRepository, never()).delete(any());
    }

    private Category createCategory(String categoryName) {

        Category category = new Category();
        category.setId(1L);
        category.setName(categoryName);

        return category;
    }
}