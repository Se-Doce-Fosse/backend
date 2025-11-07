package com.sedocefosse.backend.service.mapper;

import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {


    @Mapping(target = "valor", source = "price")
    @Mapping(target = "quantidade", source = "quantity")
    @Mapping(target = "nome", source = "name")
    @Mapping(target = "imagemUrl", source = "imageSrc")
    @Mapping(target = "descricao", source = "description")
    @Mapping(target = "ativo", source = "isActive")
    Product toEntity(ProductDTO productDTO);

}
