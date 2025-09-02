package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product findBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    public List<Product> findByAtivoTrue() {
        return productRepository.findByAtivoTrue();
    }

    public List<Product> findByAtivoFalse() {
        return productRepository.findByAtivoTrue();
    }

    public List<Product> findByDescricaoContainingIgnoreCase(String texto) {
        return productRepository.findByDescricaoContainingIgnoreCase(texto);
    }
}
