package com.sedocefosse.backend.service.shoppingCart;

import com.sedocefosse.backend.configs.exceptions.ProductException;
import com.sedocefosse.backend.configs.exceptions.ShoppingCartException;
import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.dto.OrderItemDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.shoppingCart.ShoppingCartDTO;
import com.sedocefosse.backend.model.shoppingCart.ProductShoppingCart;
import com.sedocefosse.backend.model.shoppingCart.ShoppingCartEntity;
import com.sedocefosse.backend.repository.shoppingCart.LegacyShoppingCartRepository;
import com.sedocefosse.backend.repository.shoppingCart.ShoppingCartRepository;
import com.sedocefosse.backend.service.shoppingCart.mapper.ShoppingCartMapper;
import com.sedocefosse.backend.service.order.OrderServiceImpl;
import com.sedocefosse.backend.service.products.ProductServiceImpl;
import com.sedocefosse.backend.utils.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final LegacyShoppingCartRepository legacyShoppingCartRepository;
    private final ProductServiceImpl productService;
    private final OrderServiceImpl orderService;

    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO dto) {
        log.info("Criando carrinho para cliente: {}", dto.getCellphone());
        var entity = shoppingCartMapper.dtoToEntity(dto);
        var saved = shoppingCartRepository.save(entity);
        log.info("Carrinho criado com ID {}", saved.getShoppingCartId());
        return shoppingCartMapper.entityToDto(saved);
    }

    public ShoppingCartDTO findShoppingCartByPhoneNumber(String phoneNumber) {
        log.info("Buscando carrinho pelo telefone: {}", phoneNumber);
        var cart = shoppingCartRepository.findShoppingCartEntityByCellphone(phoneNumber);
        if (cart == null) {
            log.warn("Nenhum carrinho encontrado para o telefone {}", phoneNumber);
            throw new ShoppingCartException("ShoppingCart not found");
        }
        log.info("Carrinho encontrado: {}", cart.getShoppingCartId());
        return shoppingCartMapper.entityToDto(cart);
    }

    public ShoppingCartDTO findShoppingCart(String cartId) {
        log.info("Buscando carrinho ID={}", cartId);
        var cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Carrinho {} não encontrado", cartId);
                    return new ShoppingCartException("ShoppingCart not found");
                });
        log.info("Carrinho encontrado com {} itens", cart.getItems().size());
        return shoppingCartMapper.entityToDto(cart);
    }

    public ShoppingCartDTO addProduct(String shoppingCartId, String SKU) {
        log.info("Adicionando produto {} ao carrinho {}", SKU, shoppingCartId);
        var shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> {
                    log.error("Carrinho {} não encontrado ao tentar adicionar produto", shoppingCartId);
                    return new ShoppingCartException("ShoppingCart not found");
                });

        var product = productService.findProductBySku(SKU)
                .orElseThrow(() -> {
                    log.warn("Produto {} fora de estoque", SKU);
                    return new ProductException("Product out of stock");
                });

        var existingItem = shoppingCart.getItems().stream()
                .filter(item -> item.getSku().equals(SKU))
                .findFirst();

        if (existingItem.isPresent()) {
            var item = existingItem.get();
            item.setAmount(item.getAmount() + 1);
            log.info("Quantidade do produto {} incrementada para {}", SKU, item.getAmount());
        } else {
            shoppingCart.getItems().add(createShoppingProduct(product));
            log.info("Produto {} adicionado ao carrinho {}", SKU, shoppingCartId);
        }

        updateTotal(shoppingCart);
        log.info("Total do carrinho atualizado para {}", shoppingCart.getTotal());

        var saved = shoppingCartRepository.save(shoppingCart);
        log.info("Carrinho {} salvo com sucesso", saved.getShoppingCartId());
        return shoppingCartMapper.entityToDto(saved);
    }

    public ShoppingCartDTO removeProduct(String shoppingCartId, String SKU) {
        log.info("Removendo produto {} do carrinho {}", SKU, shoppingCartId);
        var shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> {
                    log.error("Carrinho {} não encontrado ao tentar remover produto", shoppingCartId);
                    return new ShoppingCartException("ShoppingCart not found");
                });

        var itemOpt = shoppingCart.getItems().stream()
                .filter(item -> item.getSku().equals(SKU))
                .findFirst();

        if (itemOpt.isEmpty()) {
            log.warn("Produto {} não encontrado no carrinho {}", SKU, shoppingCartId);
            throw new ProductException("Product not found in cart");
        }

        var item = itemOpt.get();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
            log.info("Quantidade do produto {} reduzida para {}", SKU, item.getAmount());
        } else {
            shoppingCart.getItems().remove(item);
            log.info("Produto {} removido do carrinho {}", SKU, shoppingCartId);
        }

        updateTotal(shoppingCart);
        log.info("Total do carrinho após remoção: {}", shoppingCart.getTotal());

        var saved = shoppingCartRepository.save(shoppingCart);
        log.info("Carrinho {} salvo com sucesso após remoção", saved.getShoppingCartId());
        return shoppingCartMapper.entityToDto(saved);
    }

    public OrderDTO checkout(String cartId) {
        log.info("Iniciando checkout para carrinho {}", cartId);

        var shoppingCart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.error("Carrinho {} não encontrado no checkout", cartId);
                    return new ShoppingCartException("ShoppingCart not found");
                });

        if (shoppingCart.getItems().isEmpty()) {
            log.warn("Carrinho {} está vazio e não pode ser finalizado", cartId);
            throw new ShoppingCartException("Cannot checkout an empty cart");
        }

        var outOfStockItems = shoppingCart.getItems().stream()
                .filter(item -> {
                    var productOpt = productService.findProductBySku(item.getSku());
                    if (productOpt.isEmpty()) return true;

                    var product = productOpt.get();
                    return product.getQuantity() == null || product.getQuantity() < item.getAmount();
                })
                .map(ProductShoppingCart::getSku)
                .toList();

        if (!outOfStockItems.isEmpty()) {
            log.warn("Carrinho {} contém itens fora de estoque: {}", cartId, outOfStockItems);
            throw new ShoppingCartException("Produtos fora de estoque: " + outOfStockItems);
        }

        shoppingCart.getItems().forEach(item -> {
            var product = productService.findProductBySku(item.getSku())
                    .orElseThrow(() -> new ProductException("Produto não encontrado: " + item.getSku()));

            int newQuantity = product.getQuantity() - item.getAmount();
            product.setQuantity(Math.max(newQuantity, 0));
            productService.updateProduct(product.getSku(), product);

            log.info("Estoque atualizado para {}: {} unidades restantes", product.getSku(), product.getQuantity());
        });

        var orderItems = shoppingCart.getItems().stream()
                .map(item -> {
                    var product = productService.findProductBySku(item.getSku())
                            .orElseThrow(() -> new ProductException("Produto não encontrado: " + item.getSku()));
                    return OrderItemDTO.builder()
                            .produtoSku(product.getSku())
                            .produtoNome(product.getName())
                            .quantidade(item.getAmount())
                            .valorUnitario(product.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        var order = OrderDTO.builder()
                .clientId(shoppingCart.getClientId())
                .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .totalPrice(shoppingCart.getTotal())
                .orderStatus(OrderStatusEnum.ACEITO)
                .couponCode(shoppingCart.getCupom())
                .items(orderItems)
                .cupomId(parseCupom(shoppingCart.getCupom()))
                .build();

        log.info("Criando pedido para carrinho {} com total {}", cartId, shoppingCart.getTotal());
        var savedOrder = orderService.createOrder(order);
        log.info("Pedido criado com sucesso para o carrinho {}", cartId);

        legacyShoppingCartRepository.save(shoppingCart);
        shoppingCartRepository.deleteById(cartId);
        log.info("Carrinho {} movido para histórico e removido do repositório ativo", cartId);

        return savedOrder;
    }



    private static ProductShoppingCart createShoppingProduct(ProductDTO product) {
        log.info("Criando item de carrinho para produto {}", product.getSku());
        return ProductShoppingCart.builder()
                .sku(product.getSku())
                .price(product.getPrice())
                .amount(1)
                .description(product.getDescription())
                .imageUrl(product.getImageSrc())
                .build();
    }

    private static void updateTotal(ShoppingCartEntity cart) {
        var total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
        log.debug("Total recalculado: {}", total);
    }

//    private static OrderDTO fromCart(ShoppingCartEntity cart) {
//        log.info("Mapeando carrinho {} para OrderDTO", cart.getShoppingCartId());
//        return OrderDTO.builder()
//                .clientId(cart.getClientId())
//                .orderDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
//                .totalPrice(cart.getTotal())
//                .orderStatus(OrderStatusEnum.ACEITO)
//                .items(cart.getItems().stream()
//                        .map(ProductShoppingCart::getSku)
//                        .collect(Collectors.toList()))
//                .cupomId(parseCupom(cart.getCupom()))
//                .build();
//    }

    private static Integer parseCupom(String cupom) {
        try {
            var parsed = (cupom != null && !cupom.isBlank()) ? Integer.parseInt(cupom) : null;
            log.debug("Cupom {} parseado para {}", cupom, parsed);
            return parsed;
        } catch (NumberFormatException e) {
            log.warn("Cupom {} inválido", cupom);
            return null;
        }
    }
}
