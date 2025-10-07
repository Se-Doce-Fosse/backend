package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.service.impl.OrderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private OrderServiceImpl orderService;

    @PostMapping()
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

}
