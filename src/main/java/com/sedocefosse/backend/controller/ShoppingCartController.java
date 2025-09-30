package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody ShoppingCartDTOs.CreateCartRequest request) {
        try {
            Customer saved = shoppingCartService.createCart(request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.id = saved.getId();
            response.customerId = saved.getId();
            response.nome = saved.getNome();
            response.telefone = saved.getTelefone();
            response.carrinho = saved.getCarrinho().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.produtoID = item.getProdutoID();
                dto.quantidade = item.getQuantidade();
                dto.precoProduto = item.getPrecoProduto();
                dto.imagemProduto = item.getImagemProduto();
                dto.descricaoProduto = item.getDescricaoProduto();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getCarrinho().stream()
                .mapToDouble(i -> (i.getPrecoProduto() != null ? i.getPrecoProduto() : 0.0) * (i.getQuantidade() != null ? i.getQuantidade() : 0))
                .sum();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable String id) {
        try {
            Customer customer = shoppingCartService.getCart(id);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.id = customer.getId();
            response.customerId = customer.getId();
            response.nome = customer.getNome();
            response.telefone = customer.getTelefone();
            response.carrinho = customer.getCarrinho().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.produtoID = item.getProdutoID();
                dto.quantidade = item.getQuantidade();
                dto.precoProduto = item.getPrecoProduto();
                dto.imagemProduto = item.getImagemProduto();
                dto.descricaoProduto = item.getDescricaoProduto();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = customer.getCarrinho().stream()
                .mapToDouble(i -> (i.getPrecoProduto() != null ? i.getPrecoProduto() : 0.0) * (i.getQuantidade() != null ? i.getQuantidade() : 0))
                .sum();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable String id, @RequestBody ShoppingCartDTOs.UpdateCartRequest request) {
        try {
            Customer saved = shoppingCartService.updateCart(id, request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.id = saved.getId();
            response.customerId = saved.getId();
            response.nome = saved.getNome();
            response.telefone = saved.getTelefone();
            response.carrinho = saved.getCarrinho().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.produtoID = item.getProdutoID();
                dto.quantidade = item.getQuantidade();
                dto.precoProduto = item.getPrecoProduto();
                dto.imagemProduto = item.getImagemProduto();
                dto.descricaoProduto = item.getDescricaoProduto();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getCarrinho().stream()
                .mapToDouble(i -> (i.getPrecoProduto() != null ? i.getPrecoProduto() : 0.0) * (i.getQuantidade() != null ? i.getQuantidade() : 0))
                .sum();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable String id, @RequestBody ShoppingCartDTOs.UpdateItemRequest request) {
        try {
            Customer saved = shoppingCartService.updateCartItem(id, request);

            ShoppingCartDTOs.CartResponse response = new ShoppingCartDTOs.CartResponse();
            response.id = saved.getId();
            response.customerId = saved.getId();
            response.nome = saved.getNome();
            response.telefone = saved.getTelefone();
            response.carrinho = saved.getCarrinho().stream().map(item -> {
                ShoppingCartDTOs.CartResponse.Item dto = new ShoppingCartDTOs.CartResponse.Item();
                dto.produtoID = item.getProdutoID();
                dto.quantidade = item.getQuantidade();
                dto.precoProduto = item.getPrecoProduto();
                dto.imagemProduto = item.getImagemProduto();
                dto.descricaoProduto = item.getDescricaoProduto();
                return dto;
            }).collect(Collectors.toList());
            response.subtotal = saved.getCarrinho().stream()
                .mapToDouble(i -> (i.getPrecoProduto() != null ? i.getPrecoProduto() : 0.0) * (i.getQuantidade() != null ? i.getQuantidade() : 0))
                .sum();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


