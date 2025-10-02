package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.ShoppingCart;
import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.repository.CustomerRepository;
import com.sedocefosse.backend.repository.ProductRepository;
import com.sedocefosse.backend.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CustomerRepository customerRepository;

    public ShoppingCartServiceImpl(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ShoppingCart createCart(ShoppingCartDTOs.CreateCartRequest request) {
        ShoppingCart cart = new ShoppingCart();

        List<ShoppingCart.Item> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        if (request.items != null) {
            for (ShoppingCartDTOs.CreateCartRequest.Item reqItem : request.items) {
                Product product = productRepository.findById(reqItem.productSku)
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + reqItem.productSku));

                BigDecimal unitPrice = product.getValor() != null ? product.getValor() : BigDecimal.ZERO;
                BigDecimal qty = BigDecimal.valueOf(reqItem.quantity != null ? reqItem.quantity : 0);
                BigDecimal lineTotal = unitPrice.multiply(qty);

                ShoppingCart.Item item = new ShoppingCart.Item();
                item.setProductSku(product.getSku());
                item.setQuantity(reqItem.quantity);
                item.setUnitPrice(unitPrice);
                item.setLineTotal(lineTotal);
                items.add(item);

                subtotal = subtotal.add(lineTotal);
            }
        }

        cart.setItems(items);
        cart.setSubtotal(subtotal);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart getCart(String cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado: " + cartId));
    }

    @Override
    public ShoppingCart updateCart(String cartId, ShoppingCartDTOs.UpdateCartRequest request) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado: " + cartId));

        List<ShoppingCart.Item> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        if (request.items != null) {
            for (ShoppingCartDTOs.UpdateCartRequest.Item reqItem : request.items) {
                Product product = productRepository.findById(reqItem.productSku)
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + reqItem.productSku));

                BigDecimal unitPrice = product.getValor() != null ? product.getValor() : BigDecimal.ZERO;
                BigDecimal qty = BigDecimal.valueOf(reqItem.quantity != null ? reqItem.quantity : 0);
                BigDecimal lineTotal = unitPrice.multiply(qty);

                ShoppingCart.Item item = new ShoppingCart.Item();
                item.setProductSku(product.getSku());
                item.setQuantity(reqItem.quantity);
                item.setUnitPrice(unitPrice);
                item.setLineTotal(lineTotal);
                items.add(item);

                subtotal = subtotal.add(lineTotal);
            }
        }

        cart.setItems(items);
        cart.setSubtotal(subtotal);
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart updateCartItem(String cartId, ShoppingCartDTOs.UpdateItemRequest request) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado: " + cartId));

        Product product = productRepository.findById(request.productSku)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + request.productSku));

        // find existing item
        ShoppingCart.Item existing = cart.getItems().stream()
                .filter(i -> i.getProductSku().equals(request.productSku))
                .findFirst()
                .orElse(null);

        if (request.quantity == null || request.quantity <= 0) {
            // remove item
            if (existing != null) {
                cart.getItems().remove(existing);
            }
        } else {
            BigDecimal unitPrice = product.getValor() != null ? product.getValor() : BigDecimal.ZERO;
            BigDecimal qty = BigDecimal.valueOf(request.quantity);
            BigDecimal lineTotal = unitPrice.multiply(qty);

            if (existing != null) {
                existing.setQuantity(request.quantity);
                existing.setUnitPrice(unitPrice);
                existing.setLineTotal(lineTotal);
            } else {
                ShoppingCart.Item item = new ShoppingCart.Item();
                item.setProductSku(product.getSku());
                item.setQuantity(request.quantity);
                item.setUnitPrice(unitPrice);
                item.setLineTotal(lineTotal);
                cart.getItems().add(item);
            }
        }

        // recompute subtotal
        BigDecimal subtotal = cart.getItems().stream()
                .map(i -> i.getLineTotal() != null ? i.getLineTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public void mergeCartIntoCustomer(String cartId, String customerId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado: " + cartId));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + customerId));

        // Mesclar itens
        cart.getItems().forEach(ci -> {
            Customer.CartItem existing = customer.getCarrinho().stream()
                    .filter(it -> it.getProdutoID().equals(ci.getProductSku()))
                    .findFirst()
                    .orElse(null);
            if (existing != null) {
                existing.setQuantidade((existing.getQuantidade() != null ? existing.getQuantidade() : 0) + (ci.getQuantity() != null ? ci.getQuantity() : 0));
                existing.setPrecoProduto(ci.getUnitPrice() != null ? ci.getUnitPrice().doubleValue() : existing.getPrecoProduto());
            } else {
                Customer.CartItem ni = new Customer.CartItem();
                ni.setProdutoID(ci.getProductSku());
                ni.setQuantidade(ci.getQuantity());
                ni.setPrecoProduto(ci.getUnitPrice() != null ? ci.getUnitPrice().doubleValue() : 0.0);
                ni.setImagemProduto(null);
                ni.setDescricaoProduto(null);
                customer.getCarrinho().add(ni);
            }
        });

        customerRepository.save(customer);
        shoppingCartRepository.deleteById(cartId);
    }
}


