package com.sedocefosse.backend.service.mapper;

import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.model.shoppingCart.ShoppingCartEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCartEntity dtoToEntity(ShoppingCartDTO source);
    ShoppingCartDTO entityToDto(ShoppingCartEntity destination);
}
