package com.sedocefosse.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SupplyUpdateDTO {
    private Long id;
    private String name;
    private Long unidadeId;
    private String unidadeNome;
    private double quantidade;
    private BigDecimal precoCompra;
    private double pontoReposicao;
    private Boolean ehEmbalagem;
}
