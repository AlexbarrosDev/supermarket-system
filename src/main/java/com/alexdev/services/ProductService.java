package com.alexdev.services;

import com.alexdev.domain.entities.Category;
import com.alexdev.domain.entities.Group;
import com.alexdev.domain.enums.ProductStatus;
import com.alexdev.dtos.request.product.ProductCreateDTO;
import com.alexdev.dtos.request.product.ProductStatusUpdateDTO;
import com.alexdev.dtos.request.product.ProductUpdateDTO;
import com.alexdev.dtos.response.product.ProductDetailsDTO;
import com.alexdev.domain.entities.Product;
import com.alexdev.dtos.response.product.ProductSummaryDTO;
import com.alexdev.exceptions.BusinessException;
import com.alexdev.exceptions.ResourceNotFoundException;
import com.alexdev.mappers.ProductMapper;
import com.alexdev.repositories.CategoryRepository;
import com.alexdev.repositories.GroupRepository;
import com.alexdev.repositories.ItemSoldRepository;
import com.alexdev.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryRepository categoryRepository;

    private final GroupRepository groupRepository;

    private final ItemSoldRepository itemSoldRepository;

    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> findAllProducts() {

        List<Product> productList = productRepository.findAll();

        if (productList.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }

        return productMapper
                .productEntityListToProductSummaryDTOList(productList);
    }

    @Transactional(readOnly = true)
    public ProductDetailsDTO findProductById(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Product not found with id " + id));

        return productMapper.productEntityToProductDetailsDTO(product);
    }

    @Transactional
    public ProductDetailsDTO createProduct(ProductCreateDTO productCreateDTO) {

        Product entity = productMapper.productRequestDTOToProductEntity(productCreateDTO);

        entity.setCategory(getCategoryOrThrow(productCreateDTO.categoryId()));
        entity.setGroup(getGroupOrThrow(productCreateDTO.groupId()));

        return productMapper.productEntityToProductDetailsDTO(
                productRepository.save(entity));
    }

    @Transactional
    public ProductDetailsDTO updateProductStatus(Long id, ProductStatusUpdateDTO productStatus) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Product not found with id " + id));

        if (productStatus.status() == product.getStatus()) {

            throw new BusinessException("Product is already in the requested status.");
        }

        product.setStatus(productStatus.status());

        return productMapper.productEntityToProductDetailsDTO(product);
    }

    @Transactional
    public ProductDetailsDTO updateProduct(Long id, ProductUpdateDTO  productUpdateDTO) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Product not found with id " + id));

        product.setStatus(productUpdateDTO.status());
        product.setName(productUpdateDTO.name());
        product.setCurrentPrice(productUpdateDTO.currentPrice());
        product.setCategory(getCategoryOrThrow(productUpdateDTO.categoryId()));
        product.setGroup(getGroupOrThrow(productUpdateDTO.groupId()));

        return productMapper.productEntityToProductDetailsDTO(product);
    }

    @Transactional
    public void deleteProductById(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(()
                -> new ResourceNotFoundException("Product not found with id " + id));

        if (itemSoldRepository.existsByProductId(product.getId())) {

            throw new BusinessException(
                    "Product cannot be deleted because it has already been sold."
            );
        }

        productRepository.delete(product);
    }

    private Group getGroupOrThrow(Long id) {

        return groupRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Group not found"));
    }

    private Category getCategoryOrThrow(Long id) {

        return categoryRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Category not found"));
    }
}
