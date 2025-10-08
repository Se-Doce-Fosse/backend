package com.sedocefosse.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.Product;

@Repository 
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findBySkuIn(List<String> skus);

}