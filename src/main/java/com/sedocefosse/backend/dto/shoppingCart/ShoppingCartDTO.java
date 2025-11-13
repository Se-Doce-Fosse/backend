package com.sedocefosse.backend.dto.shoppingCart;

import com.sedocefosse.backend.model.shoppingCart.ProductShoppingCart;
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
public class ShoppingCartDTO {
    private String shoppingCartId;
    private String clientId;
    private String name;
    private String cellphone;
    private String cupom;
    @Builder.Default
    private List<ProductShoppingCartDTO> items = new ArrayList<>();
    private BigDecimal total;
}
