package com.sedocefosse.backend.model.shoppingCart;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String shoppingCartId;
    private String name;
    private String cellphone;
    private String cupom;
    @Builder.Default
    private List<ProductShoppingCart> items = new ArrayList<>();
    private BigDecimal total;
}
