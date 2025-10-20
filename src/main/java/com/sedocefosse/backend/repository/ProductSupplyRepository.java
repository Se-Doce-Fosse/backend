package com.sedocefosse.backend.repository;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.ProductSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSupplyRepository extends JpaRepository<ProductSupply, Long> {
    List<ProductSupply> findByProductSku(String sku);
}
