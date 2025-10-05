package com.sedocefosse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Unit {
    private Long id;
    private String nome;
    private double base_preco_compra;

}
