package com.sedocefosse.backend.service;

import java.util.List;
import java.util.Optional;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;

public interface ProductService {

    Product create(Product product);

    Optional<Product> findProductById(String sku);

    Optional<ProductDetailsDTO> findProductDetailsBySku(String sku); 

    void deleteProductById(String sku);

    List<CategoryDTO> getAllProductsGroupedByCategory();

}