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
    private String sku;
    private String name;
    private String price;
    private String imageSrc;
    private String description;
    private Boolean isActive;
    private Integer quantity;
    private CategoryDTO category;
    private List<String> allergens;
}