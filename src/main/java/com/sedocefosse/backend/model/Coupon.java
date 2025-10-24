package com.sedocefosse.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupom")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;
    
    @Column(name = "valor_desc", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesc;
    
    @Column(name = "validade", nullable = false)
    private LocalDate validade;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;
    
    @Column(name = "unico", nullable = false)
    private Boolean unico;
}
