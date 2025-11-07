package com.sedocefosse.backend.repository.products;

import com.sedocefosse.backend.model.ProductSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSupplyRepository extends JpaRepository<ProductSupply, Long> {
    List<ProductSupply> findByProductSku(String sku);
    Optional<ProductSupply> findByProductSkuAndSupplyId(String productSku, Long supplyId);

    @Modifying
    @Query(
            value = "DELETE FROM produto_ingrediente WHERE produto_sku = :sku AND ingrediente_id = :id",
            nativeQuery = true
    )
    void deleteByProductSkuAndSupplyId(@Param("sku") String productSku, @Param("id") Long supplyId);
}
