package com.sedocefosse.backend.service.shoppingCart;

import com.sedocefosse.backend.dto.shoppingCart.ProductShoppingCartDTO;
import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.products.ProductRepository;
import com.sedocefosse.backend.repository.shoppingCart.ShoppingCartRepository;
import com.sedocefosse.backend.service.products.ProductService;
import com.sedocefosse.backend.service.shoppingCart.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO dto) {
        return shoppingCartMapper.entityToDto(shoppingCartRepository.save(shoppingCartMapper.dtoToEntity(dto)));
    }

    public ShoppingCartDTO findShoppingCartByPhoneNumber(ShoppingCartDTO dto) {
        return shoppingCartMapper.entityToDto(
                shoppingCartRepository.findShoppingCartEntityByCellphone(dto.getCellphone()));
    }

    public ShoppingCartDTO addProduct(String shoppingCartId, String SKU) {
        final var shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (Objects.isNull(shoppingCart)) {
            shoppingCart = createShoppingCart(new ShoppingCartDTO())
        }

        final var product = productService.findProductById(SKU).orElse(null);
        if (Objects.nonNull(product)) {
            product.setQuantidade(product.getQuantidade() - 1);
            var updatedProduct = updateProduct(1, product, true);
            productService.updateProduct(SKU, updatedProduct);

        }
        return null;
    }

    public ShoppingCartDTO removeProduct(String SKU) {
        final var product = productService.findProductById(SKU);

        return null;
    }

    public ShoppingCartDTO updateCart(String cartId) {
        final var product = productService.findProductById(SKU);

        return null;
    }

    private Product updateProduct(int quantity, Product product, boolean remove) {
        var productQuantity = product.getQuantidade();
        if (remove) {
            if (productQuantity - quantity >= 0) {
                product.setQuantidade(product.getQuantidade() - quantity);
            }
            else  {
                for (int i = 0; i < productQuantity; i++) {
                    product.setQuantidade(productQuantity - i);
                }
            }
        }
        else {
            product.setQuantidade(product.getQuantidade() + quantity);
        }
        return product;
    }

    private ProductShoppingCartDTO createShoppingProduct(Product product) {
        return new ProductShoppingCartDTO().builder()
                .SKU(product.getSku())
                .price(product.getValor())
                .description(product.getDescricao())
                .imageUrl(product.getImagemUrl())
                .build();
    }
}
