package com.sedocefosse.backend.service.shoppingCart.mapper;

import com.sedocefosse.backend.dto.shoppingCart.ProductShoppingCartDTO;
import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.model.shoppingCart.ProductShoppingCart;
import com.sedocefosse.backend.model.shoppingCart.ShoppingCartEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    public ShoppingCartEntity dtoToEntity(ShoppingCartDTO dto) {
        if (dto == null) return null;

        return ShoppingCartEntity.builder()
                .shoppingCartId(dto.getShoppingCartId())
                .clientId(dto.getClientId())
                .name(dto.getName())
                .cellphone(dto.getCellphone())
                .cupom(dto.getCupom())
                .items(dtoToEntityItems(dto.getItems()))
                .total(dto.getTotal())
                .build();
    }

    public ShoppingCartDTO entityToDto(ShoppingCartEntity entity) {
        if (entity == null) return null;

        return ShoppingCartDTO.builder()
                .shoppingCartId(entity.getShoppingCartId())
                .clientId(entity.getClientId())
                .name(entity.getName())
                .cellphone(entity.getCellphone())
                .cupom(entity.getCupom())
                .items(entityToDtoItems(entity.getItems()))
                .total(entity.getTotal())
                .build();
    }

    private List<ProductShoppingCart> dtoToEntityItems(List<ProductShoppingCartDTO> items) {
        if (items == null) return List.of();

        return items.stream()
                .map(i -> ProductShoppingCart.builder()
                        .sku(i.getSKU())
                        .amount(i.getAmount())
                        .price(i.getPrice())
                        .imageUrl(i.getImageUrl())
                        .description(i.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProductShoppingCartDTO> entityToDtoItems(List<ProductShoppingCart> items) {
        if (items == null) return List.of();

        return items.stream()
                .map(i -> ProductShoppingCartDTO.builder()
                        .SKU(i.getSku())
                        .amount(i.getAmount())
                        .price(i.getPrice())
                        .imageUrl(i.getImageUrl())
                        .description(i.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
