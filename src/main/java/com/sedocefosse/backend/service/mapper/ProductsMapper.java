package com.sedocefosse.backend.service.mapper;

import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductsMapper {
    ProductDTO toProductDTO(Product product);
    ProductDetailsDTO toProductDetailsDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}
