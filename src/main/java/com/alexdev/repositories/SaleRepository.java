package com.alexdev.repositories;

import com.alexdev.domain.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    boolean existsByClientId(Long clientId);
}
