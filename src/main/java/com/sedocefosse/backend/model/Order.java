package com.sedocefosse.backend.model;

import com.sedocefosse.backend.utils.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "Pedido")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long orderId;

    @Column(name = "cliente_id", nullable = false)
    private Integer clientId;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "valor_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatusEnum orderStatus;

    @ElementCollection
    @CollectionTable(name = "pedido_produto", joinColumns = @JoinColumn(name = "pedido_id"))
    @Column(name = "produto_sku", nullable = false)
    @Builder.Default
    private List<String> products = new ArrayList<>();

    @Column(name = "cupom_id")
    private Integer cupomId;

    @PrePersist
    void prePersist() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        }
    }
}
