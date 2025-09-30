package com.sedocefosse.backend.dto;

import java.util.List;

public class ShoppingCartDTOs {

    public static class CreateCartRequest {
        public String customerId;
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
        public String id;
        public String customerId;
        public String nome;
        public String telefone;
        public List<Item> carrinho;
        public Double subtotal;

        public static class Item {
            public String produtoID;
            public Integer quantidade;
            public Double precoProduto;
            public String imagemProduto;
            public String descricaoProduto;
        }
    }
}


