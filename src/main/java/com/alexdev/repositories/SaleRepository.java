package com.alexdev.repositories;

import com.alexdev.domain.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    boolean existsByClientId(Long clientId);
}
