package com.sedocefosse.backend.repository.order;

import com.sedocefosse.backend.model.order.Order;
import com.sedocefosse.backend.utils.OrderStatusEnum;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByClientId(String clientId);

    List<Order> findByOrderStatus(OrderStatusEnum status);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM pedido_item WHERE produto_sku = :sku)", nativeQuery = true)
    boolean existsSkuInAnyOrder(@Param("sku") String sku);
}
