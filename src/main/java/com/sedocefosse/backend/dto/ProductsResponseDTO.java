package com.sedocefosse.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductsResponseDTO {
    private List<CategoryDTO> categories;
}