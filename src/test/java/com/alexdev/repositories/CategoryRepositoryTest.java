package com.alexdev.repositories;

import com.alexdev.domain.category.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void shouldReturnTrueWhenCategoryExistsByName() {

        // Arrange
        Category category =  createCategory();

        categoryRepository.save(category);

        // Act
        boolean result = categoryRepository.existsByName("Category1");

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCategoryNameDoesNotExist() {

        // Act
        boolean result = categoryRepository.existsByName("Category1");

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenCategoryExistsByNameAndIdIsTheSame() {

        // Arrange
        Category category =  createCategory();

        Category savedCategory = categoryRepository.save(category);

        // Act
        boolean result = categoryRepository
                .existsByNameAndIdNot("Category1", savedCategory.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenCategoryNameExistsAndIdIsDifferent() {

        // Arrange
        Category category =  createCategory();

        Long anotherId = Long.MAX_VALUE;

        categoryRepository.save(category);

        // Act
        boolean result = categoryRepository
                .existsByNameAndIdNot("Category1", anotherId);

        // Assert
        assertTrue(result);
    }

    private Category createCategory() {

        Category category = new Category();
        category.setName("Category1");
        return category;
    }
}