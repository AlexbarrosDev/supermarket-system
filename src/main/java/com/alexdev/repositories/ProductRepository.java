package com.alexdev.repositories;

import com.alexdev.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByGroupId(Long groupId);

    boolean existsByCategoryId(Long categoryId);
}
