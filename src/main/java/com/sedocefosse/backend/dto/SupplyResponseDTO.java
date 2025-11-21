package com.sedocefosse.backend.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SupplyResponseDTO {
    private Long id;
    private String name;
    private Long unityId;
    private String unityName;
    private double quantity;
    private BigDecimal purchasePrice;
    private double reorderPoint;
    private Boolean isPackaging;
}

