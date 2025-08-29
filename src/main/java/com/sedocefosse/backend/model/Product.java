package com.sedocefosse.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;
    
    @Column(name = "nome", length = 200)
    private String nome;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
    // Constructors
    public Product() {
    }
    
    public Product(String sku, String nome, String descricao, BigDecimal valor, String imagemUrl, Boolean ativo) {
        this.sku = sku;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.imagemUrl = imagemUrl;
        this.ativo = ativo;
    }
    
    // Getters and Setters 
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public String getnome() {
        return nome;
    }
    
    public void setnome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getImagemUrl() {
        return imagemUrl;
    }
    
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", imagemUrl='" + imagemUrl + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
