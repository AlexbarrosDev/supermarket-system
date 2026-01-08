package com.alexdev.repositories;

import com.alexdev.entities.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyItemRepository extends JpaRepository<BuyItem, Long> {
}
