package com.sedocefosse.backend.service;

import java.util.List;
import java.util.Optional;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;

public interface ProductService {

    ProductDetailsDTO create(ProductDTO product);

    Optional<ProductDTO> findProductBySku(String sku);

    Optional<ProductDTO> findProductDetailsBySku(String sku); 

    void deleteProductById(String sku);

    List<CategoryDTO> getAllProductsGroupedByCategory();

    Product toggleStatus(String id);
    
    Product updateProduct(String sku, ProductDTO product);

    List<ProductDTO> getAllProducts();
}