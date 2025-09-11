package com.sedocefosse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RelatedProductDTO {
    private String id;
    private String name;
    private String price;
    private String imageSrc;
    private String imageAlt;
}