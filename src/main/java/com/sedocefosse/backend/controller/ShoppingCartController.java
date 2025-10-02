package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.ShoppingCart;
import com.sedocefosse.backend.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody ShoppingCartDTOs.CreateCartRequest request) {
        try {
            ShoppingCart saved = shoppingCartService.createCart(request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.cartId = saved.getId();
            response.items = saved.getItems().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.productSku = item.getProductSku();
                dto.quantity = item.getQuantity();
                dto.unitPrice = item.getUnitPrice();
                dto.lineTotal = item.getLineTotal();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getSubtotal();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable String id) {
        try {
            ShoppingCart cart = shoppingCartService.getCart(id);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.cartId = cart.getId();
            response.items = cart.getItems().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.productSku = item.getProductSku();
                dto.quantity = item.getQuantity();
                dto.unitPrice = item.getUnitPrice();
                dto.lineTotal = item.getLineTotal();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = cart.getSubtotal();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable String id, @RequestBody ShoppingCartDTOs.UpdateCartRequest request) {
        try {
            ShoppingCart saved = shoppingCartService.updateCart(id, request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.cartId = saved.getId();
            response.items = saved.getItems().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.productSku = item.getProductSku();
                dto.quantity = item.getQuantity();
                dto.unitPrice = item.getUnitPrice();
                dto.lineTotal = item.getLineTotal();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getSubtotal();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable String id, @RequestBody ShoppingCartDTOs.UpdateItemRequest request) {
        try {
            ShoppingCart saved = shoppingCartService.updateCartItem(id, request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.cartId = saved.getId();
            response.items = saved.getItems().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.productSku = item.getProductSku();
                dto.quantity = item.getQuantity();
                dto.unitPrice = item.getUnitPrice();
                dto.lineTotal = item.getLineTotal();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getSubtotal();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


