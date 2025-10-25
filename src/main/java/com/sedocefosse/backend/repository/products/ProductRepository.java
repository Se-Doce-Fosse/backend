package com.sedocefosse.backend.repository.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sedocefosse.backend.model.Product;

@Repository 
public interface ProductRepository extends JpaRepository<Product, String> {

}