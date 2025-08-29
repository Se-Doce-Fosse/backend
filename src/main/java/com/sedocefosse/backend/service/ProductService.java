package com.sedocefosse.backend.service;

import java.util.Optional;

import com.sedocefosse.backend.model.Product;

public interface ProductService {

    Product create(Product product);

    Optional<Product> findProductById(Long id);

}
