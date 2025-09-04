package com.sedocefosse.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.ProductService;

@RestController
@RequestMapping("/admin/products")
public class AdminProdutoController {

    private final ProductService productService;

    public AdminProdutoController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.save(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Long id) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        product.setAtivo(!Boolean.TRUE.equals(product.getAtivo()));
        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
}

