package com.sedocefosse.backend.repository;

import com.sedocefosse.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    List<Product> findByAtivoTrue();
    
    List<Product> findByAtivoFalse();
    
    List<Product> findByDescricaoContainingIgnoreCase(String texto);
    