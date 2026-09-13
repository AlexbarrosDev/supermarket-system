package com.alexdev.repositories;

import com.alexdev.domain.category.Category;
import com.alexdev.domain.group.Group;
import com.alexdev.domain.product.Product;
import com.alexdev.domain.product.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void shouldReturnTrueWhenGroupIdExists() {

        // Arrange
        Product product = createProduct();

        productRepository.save(product);

        // Act
        boolean result = productRepository
                .existsByGroupId(product.getGroup().getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenGroupIdDoesNotExist() {

        // Arrange
        Long groupId = Long.MAX_VALUE;

        // Act
        boolean result = productRepository.existsByGroupId(groupId);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenCategoryIdExists() {

        // Arrange
        Product product = createProduct();

        productRepository.save(product);

        // Act
        boolean result = productRepository
                .existsByCategoryId(product.getCategory().getId());

        //Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCategoryIdDoesNotExist() {

        // Arrange
        Long categoryId = Long.MAX_VALUE;

        // Act
        boolean result = productRepository.existsByCategoryId(categoryId);

        // Assert
        assertFalse(result);

    }

    private Group createGroup() {

        Group group = new Group();
        group.setName("Group");

        return group;
    }

    private Category createCategory() {

        Category category = new Category();
        category.setName("Category");

        return category;
    }

    private Product createProduct() {

        Category category = createCategory();
        categoryRepository.save(category);

        Group group = createGroup();
        groupRepository.save(group);

        Product product = new Product();
        product.setName("product");
        product.setCurrentPrice(BigDecimal.valueOf(10));
        product.setCategory(category);
        product.setGroup(group);
        product.setStatus(ProductStatus.ACTIVE);

        return product;
    }
}