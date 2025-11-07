package com.sedocefosse.backend.repository.products;

import com.sedocefosse.backend.model.ProductSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSupplyRepository extends JpaRepository<ProductSupply, Long> {
    List<ProductSupply> findByProductSku(String sku);
    Optional<ProductSupply> findByProductSkuAndSupplyId(String productSku, Long supplyId);
    void deleteByProductSkuAndSupplyId(String productSku, Long supplyId);
}
