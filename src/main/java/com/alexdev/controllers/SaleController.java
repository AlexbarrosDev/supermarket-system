package com.alexdev.controllers;

import com.alexdev.dtos.request.sale.SaleCreateDTO;
import com.alexdev.dtos.request.sale.SaleStatusUpdateDTO;
import com.alexdev.dtos.response.sale.SaleDetailsDTO;
import com.alexdev.dtos.response.sale.SaleSummaryDTO;
import com.alexdev.services.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleSummaryDTO>> findAllSale() {

        return ResponseEntity.ok(saleService.findAllSale());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailsDTO> findSaleById(
            @Valid @PathVariable Long id) {

        return ResponseEntity.ok(saleService.findSaleById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDetailsDTO> createSale(
            @Valid @RequestBody SaleCreateDTO sale) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saleService.createSale(sale));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SaleDetailsDTO> updateStatus(
            @Valid @PathVariable Long id,
            @Valid @RequestBody SaleStatusUpdateDTO status
    )
    {
        return ResponseEntity.ok(saleService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteSaleById(@PathVariable Long id) {

        saleService.deleteSaleById(id);
        return ResponseEntity.noContent().build();
    }
}
