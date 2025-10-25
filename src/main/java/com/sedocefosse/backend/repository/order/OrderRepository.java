package com.sedocefosse.backend.repository.order;

import com.sedocefosse.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public boolean existsByClientId(Integer clientId);
    java.util.List<Order> findByOrderStatus(com.sedocefosse.backend.utils.OrderStatusEnum status);
}
