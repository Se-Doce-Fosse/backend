package com.sedocefosse.backend.model.order;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItemId implements Serializable {
    private Long pedidoId;
    private String produtoSku;
}
