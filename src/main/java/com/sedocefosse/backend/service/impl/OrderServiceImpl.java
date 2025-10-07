package com.sedocefosse.backend.service.impl;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.model.Order;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.OrderRepository;
import com.sedocefosse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderDTO createOrder(OrderDTO order) {
        UpdateResult result = updateProducts(order.getProducts());
        order.setOutOfStock(result.outOfStock);
        Order entity = mapToEntity(order);
        entity.setOrderId(UUID.randomUUID().toString());
        entity.setProducts(result.fulfilledProducts);
        Order saved = orderRepository.save(entity);
        return mapToDTO(saved, result.outOfStock);
    }

    private UpdateResult updateProducts(List<String> requestedProducts) {
        Map<String, Long> requestedCounts = requestedProducts.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        List<String> outOfStock = new ArrayList<>();
        List<String> fulfilledProducts = new ArrayList<>();

        for (var entry : requestedCounts.entrySet()) {
            String sku = entry.getKey();
            long requestedQty = entry.getValue();
            Product product = productService.findProductById(sku).orElse(null);
            int available = product == null ? 0 : product.getQuantidade();
            long fulfill = Math.min(available, requestedQty);
            long missing = requestedQty - fulfill;

            if (fulfill > 0 && Objects.nonNull(product)) {
                product.setQuantidade(available - (int) fulfill);
                productService.updateProduct(product.getSku(), product);
                for (int i = 0; i < fulfill; i++) fulfilledProducts.add(sku);
            }
            for (int i = 0; i < missing; i++) outOfStock.add(sku);
        }

        return new UpdateResult(outOfStock, fulfilledProducts);
    }

    private Order mapToEntity(OrderDTO orderDTO) {
        return Order.builder()
                .orderStatus(orderDTO.getOrderStatus())
                .orderDate(orderDTO.getOrderDate())
                .totalPrice(orderDTO.getTotalPrice())
                .products(orderDTO.getProducts())
                .cupom(orderDTO.getCupom())
                .clientId(orderDTO.getClientId())
                .build();
    }

    private OrderDTO mapToDTO(Order order, List<String> outOfStock) {
        return OrderDTO.builder()
                .clientId(order.getClientId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .products(order.getProducts())
                .cupom(order.getCupom())
                .outOfStock(outOfStock != null ? outOfStock : List.of())
                .build();
    }

    private static class UpdateResult {
        final List<String> outOfStock;
        final List<String> fulfilledProducts;

        UpdateResult(List<String> outOfStock, List<String> fulfilledProducts) {
            this.outOfStock = outOfStock;
            this.fulfilledProducts = fulfilledProducts;
        }
    }
}
