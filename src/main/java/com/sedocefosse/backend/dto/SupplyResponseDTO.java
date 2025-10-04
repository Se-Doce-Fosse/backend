package com.sedocefosse.backend.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SupplyResponseDTO {
    public SupplyResponseDTO() {}
    private Long id;
    private String nome;
    private Long unidadeId;
    private String unidadeNome; // Optional
    private double quantidade;
    private BigDecimal precoCompra;
    private double pontoReposicao;
}

