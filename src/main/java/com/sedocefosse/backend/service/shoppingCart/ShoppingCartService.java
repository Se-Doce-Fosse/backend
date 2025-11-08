package com.sedocefosse.backend.service.shoppingCart;

import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.repository.shoppingCart.ShoppingCartRepository;
import com.sedocefosse.backend.service.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO dto) {
        return shoppingCartMapper.entityToDto(shoppingCartRepository.save(shoppingCartMapper.dtoToEntity(dto)));
    }

    public ShoppingCartDTO findShoppingCartByPhoneNumber(ShoppingCartDTO dto) {
        return shoppingCartMapper.entityToDto(
                shoppingCartRepository.findShoppingCartEntityByCellphone(dto.getCellphone()));
    }

}
