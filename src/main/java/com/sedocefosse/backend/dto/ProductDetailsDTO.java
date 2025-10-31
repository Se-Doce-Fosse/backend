package com.sedocefosse.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailsDTO {
    private String id;
    private String name;
    private String price;
    private String imageSrc;
    private String description;
    private List<String> allergens;
    private List<RelatedProductDTO> relatedProducts;
}