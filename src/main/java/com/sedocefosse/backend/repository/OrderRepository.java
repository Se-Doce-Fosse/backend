package com.sedocefosse.backend.repository;

import com.sedocefosse.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByProductsContaining(String produtoSku);
}
