package com.sedocefosse.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private String id;
    private String name;
    private String price;
    private String imageSrc;
    private String imageAlt;
    private String description;
    private Boolean isActive;
    private String categoryId;
    private String categoryName;
    private List<String> allergens;
    private List<RelatedProductDTO> relatedProducts;
}