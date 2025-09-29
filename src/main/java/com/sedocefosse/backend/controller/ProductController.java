package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.ProductsResponseDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.ProductService;
import com.sedocefosse.backend.service.ProductServiceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    //http://localhost:8081/products
    @GetMapping
    public ResponseEntity<ProductsResponseDTO> getAllProducts() {
        List<CategoryDTO> categories = productService.getAllProductsGroupedByCategory();
        ProductsResponseDTO response = new ProductsResponseDTO(categories);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8081/products/1
    @GetMapping("/{sku}")
    public ResponseEntity<ProductDetailsDTO> getProductBySku(@PathVariable String sku) {
        Optional<ProductDetailsDTO> productDto = productService.findProductDetailsBySku(sku);

        return productDto.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    //http://localhost:8081/products/{sku}
    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable String sku, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(sku, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }
}