package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDTO {
    private String id;
    private String name;
    private String price;
    private String imageSrc;
    private String description;
    @Builder.Default
    private List<String> allergens =  new ArrayList<>();
    @Builder.Default
    private List<RelatedProductDTO> relatedProducts = new  ArrayList<>();
}