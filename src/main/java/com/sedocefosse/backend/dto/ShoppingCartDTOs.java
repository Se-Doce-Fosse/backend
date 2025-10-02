package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartDTOs {

    public static class CreateCartRequest {
        public List<Item> items;

        public static class Item {
            public String productSku;
            public Integer quantity;
        }
    }

    public static class UpdateCartRequest {
        public List<Item> items;

        public static class Item {
            public String productSku;
            public Integer quantity;
        }
    }

    public static class UpdateItemRequest {
        public String productSku;
        public Integer quantity;
    }


    public static class CartResponse {
        public String cartId;
        public List<Item> items;
        public BigDecimal subtotal;

        public static class Item {
            public String productSku;
            public Integer quantity;
            public BigDecimal unitPrice;
            public BigDecimal lineTotal;
        }
    }
}


