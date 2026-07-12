package com.alexdev.repositories;

import com.alexdev.domain.entities.ItemSold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSoldRepository extends JpaRepository<ItemSold, Long> {

    boolean existsByProductId(Long productId);

    boolean existsBySaleId(Long saleId);
}
