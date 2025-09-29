package com.sedocefosse.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
public class Customer {

    @Id
    private String id;
    private String nome;
    private String telefone;
    private List<CartItem> carrinho;

    public Customer() {
        this.carrinho = new ArrayList<>();
    }

    public Customer(String id, String nome, String telefone, List<CartItem> carrinho) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.carrinho = carrinho != null ? carrinho : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<CartItem> getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(List<CartItem> carrinho) {
        this.carrinho = carrinho;
    }

    public static class CartItem {
        private String produtoID;
        private Integer quantidade;
        private Double precoProduto;
        private String imagemProduto;
        private String descricaoProduto;

        public CartItem() {
        }

        public CartItem(String produtoID, Integer quantidade, Double precoProduto, String imagemProduto, String descricaoProduto) {
            this.produtoID = produtoID;
            this.quantidade = quantidade;
            this.precoProduto = precoProduto;
            this.imagemProduto = imagemProduto;
            this.descricaoProduto = descricaoProduto;
        }

        public String getProdutoID() {
            return produtoID;
        }

        public void setProdutoID(String produtoID) {
            this.produtoID = produtoID;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public Double getPrecoProduto() {
            return precoProduto;
        }

        public void setPrecoProduto(Double precoProduto) {
            this.precoProduto = precoProduto;
        }

        public String getImagemProduto() {
            return imagemProduto;
        }

        public void setImagemProduto(String imagemProduto) {
            this.imagemProduto = imagemProduto;
        }

        public String getDescricaoProduto() {
            return descricaoProduto;
        }

        public void setDescricaoProduto(String descricaoProduto) {
            this.descricaoProduto = descricaoProduto;
        }
    }
}


