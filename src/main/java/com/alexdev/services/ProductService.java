package com.alexdev.services;

import com.alexdev.dto.request.ProductCreateDTO;
import com.alexdev.dto.response.ProductDTO;

import com.alexdev.entities.Category;
import com.alexdev.entities.Mark;
import com.alexdev.entities.Product;

import com.alexdev.mappers.ProductMapper;

import com.alexdev.repositories.BuyItemRepository;
import com.alexdev.repositories.CategoryRepository;
import com.alexdev.repositories.MarkRepository;
import com.alexdev.repositories.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MarkRepository markRepository;
    private final BuyItemRepository buyItemRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          MarkRepository markRepository,
                          BuyItemRepository buyItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.markRepository = markRepository;
        this.buyItemRepository = buyItemRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::entityToDTO)
                .toList();

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return ProductMapper.entityToDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (buyItemRepository.existsByProductId(id)) {
            throw new IllegalStateException(
                    "Cannot delete product: it is being used by buyItem");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductDTO create(ProductCreateDTO productCreateDTO) {
        Mark mark = markRepository.findById(productCreateDTO.mark().getId())
                .orElseThrow(() -> new IllegalArgumentException("Mark not found"));

        Category category = categoryRepository.findById(productCreateDTO.category().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = new Product(productCreateDTO.name(),
                productCreateDTO.price(),
                category,
                mark);
        return ProductMapper.entityToDTO(productRepository.save(product));
    }
}
