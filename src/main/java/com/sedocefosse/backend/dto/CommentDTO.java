package com.sedocefosse.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private int nota;
    private String descricao;
    private String nomeExibicao;
}