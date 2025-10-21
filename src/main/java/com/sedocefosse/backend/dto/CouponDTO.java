package com.sedocefosse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private Long id;
    private String codigo;
    private BigDecimal valorDesc;
    private LocalDate validade;
    private Boolean ativo;
    private Boolean unico;
}
