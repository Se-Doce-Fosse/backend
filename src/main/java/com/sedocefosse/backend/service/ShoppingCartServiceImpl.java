package com.sedocefosse.backend.service;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.CustomerRepository;
import com.sedocefosse.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ShoppingCartServiceImpl(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCart(ShoppingCartDTOs.CreateCartRequest request) {
        // Buscar customer existente
        Customer customer = customerRepository.findById(request.customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + request.customerId));

        // Adicionar novos itens ao carrinho existente (não limpar)
        if (request.items != null) {
            for (ShoppingCartDTOs.CreateCartRequest.Item reqItem : request.items) {
                Product product = productRepository.findById(reqItem.productSku)
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + reqItem.productSku));

                // Verificar se produto já existe no carrinho
                Customer.CartItem existingItem = customer.getCarrinho().stream()
                        .filter(item -> item.getProdutoID().equals(product.getSku()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    // Se já existe, somar a quantidade
                    existingItem.setQuantidade(existingItem.getQuantidade() + reqItem.quantity);
                } else {
                    // Se não existe, criar novo item
                    Customer.CartItem cartItem = new Customer.CartItem();
                    cartItem.setProdutoID(product.getSku());
                    cartItem.setQuantidade(reqItem.quantity);
                    cartItem.setPrecoProduto(product.getValor().doubleValue());
                    cartItem.setImagemProduto(product.getImagemUrl());
                    cartItem.setDescricaoProduto(product.getDescricao());
                    customer.getCarrinho().add(cartItem);
                }
            }
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCart(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + customerId));
    }

    @Override
    public Customer updateCart(String customerId, ShoppingCartDTOs.UpdateCartRequest request) {
        // Buscar customer existente
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + customerId));

        // Limpar carrinho atual e adicionar novos itens
        customer.getCarrinho().clear();

        if (request.items != null) {
            for (ShoppingCartDTOs.UpdateCartRequest.Item reqItem : request.items) {
                Product product = productRepository.findById(reqItem.productSku)
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + reqItem.productSku));

                Customer.CartItem cartItem = new Customer.CartItem();
                cartItem.setProdutoID(product.getSku());
                cartItem.setQuantidade(reqItem.quantity);
                cartItem.setPrecoProduto(product.getValor().doubleValue());
                cartItem.setImagemProduto(product.getImagemUrl());
                cartItem.setDescricaoProduto(product.getDescricao());

                customer.getCarrinho().add(cartItem);
            }
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCartItem(String customerId, ShoppingCartDTOs.UpdateItemRequest request) {
        // Buscar customer existente
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + customerId));

        // Verificar se produto existe
        Product product = productRepository.findById(request.productSku)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + request.productSku));

        // Buscar item existente no carrinho
        Customer.CartItem existingItem = customer.getCarrinho().stream()
                .filter(item -> item.getProdutoID().equals(request.productSku))
                .findFirst()
                .orElse(null);

        if (request.quantity == null || request.quantity <= 0) {
            // Remove item se quantidade for 0 ou nula
            if (existingItem != null) {
                customer.getCarrinho().remove(existingItem);
            }
        } else {
            if (existingItem != null) {
                // Atualiza quantidade do item existente
                existingItem.setQuantidade(request.quantity);
                existingItem.setPrecoProduto(product.getValor().doubleValue());
                existingItem.setImagemProduto(product.getImagemUrl());
                existingItem.setDescricaoProduto(product.getDescricao());
            } else {
                // Adiciona novo item
                Customer.CartItem cartItem = new Customer.CartItem();
                cartItem.setProdutoID(product.getSku());
                cartItem.setQuantidade(request.quantity);
                cartItem.setPrecoProduto(product.getValor().doubleValue());
                cartItem.setImagemProduto(product.getImagemUrl());
                cartItem.setDescricaoProduto(product.getDescricao());
                customer.getCarrinho().add(cartItem);
            }
        }

        return customerRepository.save(customer);
    }
}


