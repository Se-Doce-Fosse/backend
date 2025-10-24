package com.sedocefosse.backend.service;

import com.sedocefosse.backend.model.Product;

public interface ProductSupplyService {
    void updateSupplyInventory(String productSku, Integer quantity);
}
