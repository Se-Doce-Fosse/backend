package com.sedocefosse.backend.service.order;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.dto.OrderItemDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.model.order.Order;
import com.sedocefosse.backend.model.order.OrderItem;
import com.sedocefosse.backend.model.order.OrderItemId;
import com.sedocefosse.backend.repository.order.OrderRepository;
import com.sedocefosse.backend.service.products.ProductService;
import com.sedocefosse.backend.utils.OrderStatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("Criando pedido para cliente {}", orderDTO.getClientId());

        UpdateResult result = updateProducts(orderDTO.getItems());
        if (!result.outOfStock.isEmpty()) {
            var skus = result.outOfStock.stream()
                    .map(OrderItemDTO::getProdutoSku)
                    .collect(Collectors.toList());
            throw new RuntimeException("Produtos fora de estoque: " + skus);
        }

        Order order = Order.builder()
                .clientId(orderDTO.getClientId())
                .orderDate(orderDTO.getOrderDate())
                .totalPrice(orderDTO.getTotalPrice())
                .orderStatus(orderDTO.getOrderStatus())
                .cupomId(orderDTO.getCupomId())
                .build();
        order = orderRepository.save(order);

        for (OrderItemDTO itemDTO : result.fulfilledProducts) {
            var id = new OrderItemId(order.getOrderId(), itemDTO.getProdutoSku());
            var item = OrderItem.builder()
                    .id(id)
                    .pedido(order)
                    .quantidade(itemDTO.getQuantidade())
                    .valorUnitario(itemDTO.getValorUnitario())
                    .build();

            entityManager.persist(item);
        }

        log.info("Pedido {} criado com sucesso", order.getOrderId());

        return OrderDTO.builder()
                .clientId(order.getClientId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .cupomId(order.getCupomId())
                .items(result.fulfilledProducts)
                .build();
    }

    private UpdateResult updateProducts(List<OrderItemDTO> requestedItems) {
        List<OrderItemDTO> outOfStock = new java.util.ArrayList<>();
        List<OrderItemDTO> fulfilled = new java.util.ArrayList<>();

        for (OrderItemDTO item : requestedItems) {
            String sku = item.getProdutoSku();
            int requestedQty = item.getQuantidade();

            ProductDTO product = productService.findProductBySku(sku).orElse(null);
            int available = (product != null && product.getQuantity() != null) ? product.getQuantity() : 0;

            if (product != null && available >= requestedQty) {
                product.setQuantity(available - requestedQty);
                productService.updateProduct(product.getSku(), product);
                fulfilled.add(OrderItemDTO.builder()
                        .produtoSku(sku)
                        .quantidade(requestedQty)
                        .valorUnitario(product.getPrice())
                        .build());
            } else {
                outOfStock.add(OrderItemDTO.builder()
                        .produtoSku(sku)
                        .quantidade(requestedQty)
                        .valorUnitario(product != null ? product.getPrice() : null)
                        .build());
            }
        }

        return new UpdateResult(outOfStock, fulfilled);
    }

    public List<OrderDTO> findByStatus(OrderStatusEnum status) {
        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .clientId(order.getClientId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .cupomId(order.getCupomId())
                .build();
    }

    private static class UpdateResult {
        final List<OrderItemDTO> outOfStock;
        final List<OrderItemDTO> fulfilledProducts;

        UpdateResult(List<OrderItemDTO> outOfStock, List<OrderItemDTO> fulfilledProducts) {
            this.outOfStock = outOfStock;
            this.fulfilledProducts = fulfilledProducts;
        }
    }
}
