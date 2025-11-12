package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private String sku;
    private String name;
    private String price;
    private String imageSrc;
    private String description;
    private Boolean isActive;
    private Integer quantity;
    @Builder.Default
    private CategoryDTO category = null;
    @Builder.Default
    private List<String> allergens =  new ArrayList<>();
    @Builder.Default
    private List<ProductSupplyDTO> productSupply =  new ArrayList<>();
}