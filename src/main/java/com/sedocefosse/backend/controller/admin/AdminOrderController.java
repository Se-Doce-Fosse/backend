package com.sedocefosse.backend.controller.admin;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.dto.UpdateOrderStatusRequest;
import com.sedocefosse.backend.service.order.OrderServiceImpl;
import com.sedocefosse.backend.utils.OrderStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/order")
public class AdminOrderController {

    private final OrderServiceImpl orderService;

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

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        if (request == null || request.getStatus() == null || request.getStatus().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        OrderStatusEnum enumStatus;
        try {
            enumStatus = OrderStatusEnum.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return orderService.updateOrderStatus(orderId, enumStatus)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
