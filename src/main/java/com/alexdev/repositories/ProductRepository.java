package com.alexdev.repositories;

import com.alexdev.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByMarkId(Long markId);

    boolean existsByCategoryId(Long categoryId);
}
