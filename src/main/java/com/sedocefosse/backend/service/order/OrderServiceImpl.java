package com.sedocefosse.backend.service.order;

import com.sedocefosse.backend.dto.OrderDTO;
import com.sedocefosse.backend.dto.OrderItemDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.model.order.Order;
import com.sedocefosse.backend.model.order.OrderItem;
import com.sedocefosse.backend.model.order.OrderItemId;
import com.sedocefosse.backend.repository.customer.CustomerRepository;
import com.sedocefosse.backend.repository.order.OrderRepository;
import com.sedocefosse.backend.repository.products.CouponRepository;
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
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;

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
                .address(orderDTO.getAddress())
                .couponCode(orderDTO.getCouponCode())
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
                .orderId(order.getOrderId())
                .clientId(order.getClientId())
                .clientName(resolveClientName(order.getClientId()))
                .address(order.getAddress())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .cupomId(order.getCupomId())
                .couponCode(resolveCouponCode(order.getCupomId(), order.getCouponCode()))
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
                        .produtoNome(product.getName())
                        .quantidade(requestedQty)
                        .valorUnitario(product.getPrice())
                        .build());
            } else {
                outOfStock.add(OrderItemDTO.builder()
                        .produtoSku(sku)
                        .produtoNome(product != null ? product.getName() : null)
                        .quantidade(requestedQty)
                        .valorUnitario(product != null ? product.getPrice() : null)
                        .build());
            }
        }

        return new UpdateResult(outOfStock, fulfilled);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findByStatus(OrderStatusEnum status) {
        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .clientId(order.getClientId())
                .clientName(resolveClientName(order.getClientId()))
                .address(order.getAddress())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .cupomId(order.getCupomId())
                .couponCode(resolveCouponCode(order.getCupomId(), order.getCouponCode()))
                .items(order.getProducts().stream()
                        .map(this::toItemDTO)
                        .toList())
                .build();
    }

    private OrderItemDTO toItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .produtoSku(orderItem.getId().getProdutoSku())
                .produtoNome(resolveProductName(orderItem.getId().getProdutoSku()))
                .quantidade(orderItem.getQuantidade())
                .valorUnitario(orderItem.getValorUnitario())
                .build();
    }

    private String resolveProductName(String sku) {
        return productService.findProductBySku(sku)
                .map(ProductDTO::getName)
                .orElse(sku);
    }

    private String resolveClientName(String clientId) {
        if (clientId == null) {
            return null;
        }

        return customerRepository.findById(clientId)
                .map(customer -> customer.getNome())
                .orElse(clientId);
    }

    private String resolveCouponCode(Integer couponId, String fallbackCode) {
        if (fallbackCode != null && !fallbackCode.isBlank()) {
            return fallbackCode;
        }

        if (couponId == null) {
            return null;
        }

        return couponRepository.findById(Long.valueOf(couponId))
                .map(coupon -> coupon.getCodigo())
                .orElse(null);
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
