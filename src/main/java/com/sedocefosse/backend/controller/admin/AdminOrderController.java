package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.service.order.OrderService;
import com.sedocefosse.backend.utils.OrderStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        OrderStatusEnum enumStatus;
        try {
            enumStatus = OrderStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        List<OrderDTO> orders = orderService.findByStatus(enumStatus);
        return ResponseEntity.ok(orders);
    }

}
