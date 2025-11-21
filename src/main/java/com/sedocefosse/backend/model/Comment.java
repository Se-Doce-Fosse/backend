package com.sedocefosse.backend.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "avaliacao")
@Builder
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "pedido_id")
    private Long pedidoId;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "nota", nullable = false)
    private int nota;

    @Column(name = "descricao", nullable = false, length = 2000)
    private String descricao;

    @Column(name = "nome_exibicao", nullable = false, length = 25)
    private String nomeExibicao;
}
