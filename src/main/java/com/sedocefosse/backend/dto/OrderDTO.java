package com.sedocefosse.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sedocefosse.backend.utils.OrderStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Long orderId;
    private String clientId;
    private String clientName;
    private String address;
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    private BigDecimal totalPrice;
    private OrderStatusEnum orderStatus;
    @Builder.Default
    private List<OrderItemDTO> items = new ArrayList<>();
    private Integer cupomId;
    private String couponCode;
    @Builder.Default
    private List<OrderItemDTO> outOfStock = new ArrayList<>();
}
