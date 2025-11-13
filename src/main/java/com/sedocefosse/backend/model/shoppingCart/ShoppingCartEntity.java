package com.sedocefosse.backend.model.shoppingCart;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shopping_cart")
public class ShoppingCartEntity {

    @Id
    @Builder.Default
    private String shoppingCartId = UUID.randomUUID().toString();
    private String clientId;
    private String name;
    private String cellphone;
    private String cupom;
    @Builder.Default
    private List<ProductShoppingCart> items = new ArrayList<>();
    private BigDecimal total;
}
