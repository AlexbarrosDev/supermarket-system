package com.alexdev.mappers;

import com.alexdev.dto.response.ProductDTO;
import com.alexdev.entities.Product;

public class ProductMapper {

    public static ProductDTO entityToDTO(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(product.getId(),
                product.getName(),
                product.getPrice(),
                CategoryMapper.entityToDTO(product.getCategory()),
                MarkMapper.entityToDTO(product.getMark()));
    }
}
