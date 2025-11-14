package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyLogDTO {
    private Long id;
    private Long insumoId;
    private String nomeInsumo;
    private int quantidade;
    private BigDecimal preco_compra;
    private String status; // "entrada" ou "saída"
    private LocalDateTime orderDate;
}