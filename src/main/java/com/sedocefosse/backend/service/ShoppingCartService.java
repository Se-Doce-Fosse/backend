package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.Customer;

public interface ShoppingCartService {
    Customer createCart(ShoppingCartDTOs.CreateCartRequest request);
    Customer getCart(String customerId);
    Customer updateCart(String customerId, ShoppingCartDTOs.UpdateCartRequest request);
    Customer updateCartItem(String customerId, ShoppingCartDTOs.UpdateItemRequest request);
}


