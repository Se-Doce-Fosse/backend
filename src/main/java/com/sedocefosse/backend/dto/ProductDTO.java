package com.sedocefosse.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private String id;
    private String name;
    private String price;
    private String imageSrc;
    private String imageAlt;
    private Boolean isActive;
    private List<String> allergens;
    private List<RelatedProductDTO> relatedProducts;
}