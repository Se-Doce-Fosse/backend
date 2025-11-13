package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.service.shoppingCart.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<ShoppingCartDTO> createShoppingCart(@RequestBody ShoppingCartDTO dto) {
        var created = shoppingCartService.createShoppingCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCartDTO> findShoppingCart(@PathVariable String cartId) {
        var cart = shoppingCartService.findShoppingCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<ShoppingCartDTO> findShoppingCartByPhone(@PathVariable String phoneNumber) {
        var cart = shoppingCartService.findShoppingCartByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{cartId}/add/{sku}")
    public ResponseEntity<ShoppingCartDTO> addProduct(@PathVariable String cartId, @PathVariable String sku) {
        var updated = shoppingCartService.addProduct(cartId, sku);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{cartId}/remove/{sku}")
    public ResponseEntity<ShoppingCartDTO> removeProduct(@PathVariable String cartId, @PathVariable String sku) {
        var updated = shoppingCartService.removeProduct(cartId, sku);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<OrderDTO> checkout(@PathVariable String cartId) {
        var order = shoppingCartService.checkout(cartId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
