package com.alexdev.repositories;

import com.alexdev.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByGroupId(Long groupId);

    boolean existsByCategoryId(Long categoryId);
}
