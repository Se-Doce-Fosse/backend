package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;

public interface ProductService {

    ProductDetailsDTO create(Product product);

    Optional<Product> findProductById(String sku);

    Optional<ProductDetailsDTO> findProductDetailsBySku(String sku); 

    void deleteProductById(String sku);

    List<CategoryDTO> getAllProductsGroupedByCategory();

    Product toggleStatus(String id);
}