package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailsDTO {
    private String sku;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private String imagemUrl;
    private Boolean ativo;
    private String categoriaNome;
}