package com.sedocefosse.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "supply_log")
@Builder
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class SupplyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "id_insumo", nullable = false)
    private Long insumoId;

    @Column(name = "nome_insumo", nullable = false, length = 50)
    private String nomeInsumo;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "preco_compra", nullable = false)
    private BigDecimal preco_compra;

    @Column(name = "status", nullable = false)
    private Boolean descricao;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime orderDate;
}