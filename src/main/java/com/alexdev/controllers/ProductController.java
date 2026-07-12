package com.alexdev.controllers;

import com.alexdev.dtos.request.product.ProductCreateDTO;
import com.alexdev.dtos.request.product.ProductStatusUpdateDTO;
import com.alexdev.dtos.request.product.ProductUpdateDTO;
import com.alexdev.dtos.response.product.ProductDetailsDTO;
import com.alexdev.dtos.response.product.ProductSummaryDTO;
import com.alexdev.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Products",
        description = "Operations related to products"
)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> findAllProducts() {

        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDTO> findProductById(
            @Valid @PathVariable Long id) {

        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO productCreateDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailsDTO> updateProduct(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO
    ){

        return ResponseEntity.ok(productService.updateProduct(id,productUpdateDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductDetailsDTO> updateProductStatus(
            @Valid @PathVariable Long id,
            @Valid @RequestBody ProductStatusUpdateDTO productStatus
    ){

        return ResponseEntity.ok(productService.updateProductStatus(id, productStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteProductById(@PathVariable Long id) {

        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
