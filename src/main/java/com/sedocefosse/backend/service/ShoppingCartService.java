package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart createCart(ShoppingCartDTOs.CreateCartRequest request);
    ShoppingCart getCart(String cartId);
    ShoppingCart updateCart(String cartId, ShoppingCartDTOs.UpdateCartRequest request);
    ShoppingCart updateCartItem(String cartId, ShoppingCartDTOs.UpdateItemRequest request);
    void mergeCartIntoCustomer(String cartId, String customerId);
}


