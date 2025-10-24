package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.utils.OrderStatusEnum;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    List<OrderDTO> findByStatus(OrderStatusEnum status);
}
