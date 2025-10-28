package com.sedocefosse.backend.model.shoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductShoppingCart {
    private String productId;
    private Integer amount;
    private BigDecimal price;
    private String imageUrl;
    private String description;
}
