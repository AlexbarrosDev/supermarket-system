package com.alexdev.mappers;

import com.alexdev.dtos.request.product.ProductCreateDTO;
import com.alexdev.dtos.response.product.ProductSummaryDTO;
import com.alexdev.dtos.response.product.ProductDetailsDTO;
import com.alexdev.domain.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CategoryMapper.class,
                GroupMapper.class
        })
public interface ProductMapper {


    Product productRequestDTOToProductEntity(ProductCreateDTO productCreateDTO);

    ProductDetailsDTO productEntityToProductDetailsDTO(Product product);

    ProductSummaryDTO productEntityToProductSummaryDTO(Product product);

    List<Product> productRequestDTOListToProductEntityList(List<ProductCreateDTO> productsRequestDTO);

    List<ProductDetailsDTO> productEntityListToProductDetailsDTOList(List<Product> products);

    List<ProductSummaryDTO> productEntityListToProductSummaryDTOList(List<Product> products);
}