package com.alexdev.repositories;

import com.alexdev.entities.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRepository extends JpaRepository<Buy, Long> {
}
