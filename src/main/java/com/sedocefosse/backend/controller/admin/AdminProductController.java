package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String sku) {
        productService.deleteProductById(sku);
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDTO> createProduct(@RequestBody ProductDTO product) {
        ProductDetailsDTO createdProduct = productService.create(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
  
    @PatchMapping("/{id}/status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable String id) {
        Product updatedProduct = productService.toggleStatus(id);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }
    
    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable String sku, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(sku, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }
}