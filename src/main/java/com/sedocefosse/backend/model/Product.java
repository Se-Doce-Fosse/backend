package com.sedocefosse.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "produto")
@Builder
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Product {
    
    @Id
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

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    @Column(name = "quantidade")
    private Integer quantidade;

}