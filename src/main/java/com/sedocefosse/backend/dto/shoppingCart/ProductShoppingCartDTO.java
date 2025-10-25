package com.sedocefosse.backend.dto.shoppingCart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductShoppingCartDTO {
    private String productId;
    private Integer amount;
    private BigDecimal price;
    private String imageUrl;
    private String description;
}
