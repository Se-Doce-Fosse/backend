package com.sedocefosse.backend.dto;

import com.sedocefosse.backend.utils.UpdateProductSupplyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductSupplyDTO {
    private Long supplyId;
    private double quantity;
    private UpdateProductSupplyEnum productSupplyEnum;
}
