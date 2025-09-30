package com.sedocefosse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedocefosse.backend.configs.security.TokenService;
import com.sedocefosse.backend.dto.ShoppingCartDTOs;
import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.repository.AdminRepository;
import com.sedocefosse.backend.service.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AdminRepository adminRepository;

    @Test
    void createCart_shouldReturn201AndCartData() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        customer.setNome("João Silva");
        customer.setTelefone("11999999999");

        Customer.CartItem cartItem = new Customer.CartItem();
        cartItem.setProdutoID("1");
        cartItem.setQuantidade(2);
        cartItem.setPrecoProduto(15.0);
        cartItem.setImagemProduto("/images/cookie-oreo.jpg");
        cartItem.setDescricaoProduto("Cookie Oreo com Nutella");
        
        customer.setCarrinho(Arrays.asList(cartItem));

        when(shoppingCartService.createCart(any())).thenReturn(customer);

        ShoppingCartDTOs.CreateCartRequest request = new ShoppingCartDTOs.CreateCartRequest();
        request.customerId = "cust1";
        ShoppingCartDTOs.CreateCartRequest.Item reqItem = new ShoppingCartDTOs.CreateCartRequest.Item();
        reqItem.productSku = "1";
        reqItem.quantity = 2;
        request.items = Arrays.asList(reqItem);

        mockMvc.perform(post("/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("cust1"))
            .andExpect(jsonPath("$.customerId").value("cust1"))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.carrinho[0].produtoID").value("1"))
            .andExpect(jsonPath("$.carrinho[0].quantidade").value(2))
            .andExpect(jsonPath("$.carrinho[0].precoProduto").value(15.0))
            .andExpect(jsonPath("$.carrinho[0].imagemProduto").value("/images/cookie-oreo.jpg"))
            .andExpect(jsonPath("$.carrinho[0].descricaoProduto").value("Cookie Oreo com Nutella"))
            .andExpect(jsonPath("$.subtotal").value(30.0));
    }

    @Test
    void createCart_shouldReturn400_whenCustomerNotFound() throws Exception {
        when(shoppingCartService.createCart(any())).thenThrow(new IllegalArgumentException("Cliente não encontrado: cust999"));

        ShoppingCartDTOs.CreateCartRequest request = new ShoppingCartDTOs.CreateCartRequest();
        request.customerId = "cust999";
        ShoppingCartDTOs.CreateCartRequest.Item reqItem = new ShoppingCartDTOs.CreateCartRequest.Item();
        reqItem.productSku = "1";
        reqItem.quantity = 2;
        request.items = Arrays.asList(reqItem);

        mockMvc.perform(post("/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getCart_shouldReturn200AndCartData() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        customer.setNome("João Silva");
        customer.setTelefone("11999999999");

        Customer.CartItem cartItem = new Customer.CartItem();
        cartItem.setProdutoID("1");
        cartItem.setQuantidade(2);
        cartItem.setPrecoProduto(15.0);
        cartItem.setImagemProduto("/images/cookie-oreo.jpg");
        cartItem.setDescricaoProduto("Cookie Oreo com Nutella");
        
        customer.setCarrinho(Arrays.asList(cartItem));

        when(shoppingCartService.getCart("cust1")).thenReturn(customer);

        mockMvc.perform(get("/shopping-cart/cust1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("cust1"))
            .andExpect(jsonPath("$.customerId").value("cust1"))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.carrinho[0].produtoID").value("1"))
            .andExpect(jsonPath("$.carrinho[0].quantidade").value(2))
            .andExpect(jsonPath("$.carrinho[0].precoProduto").value(15.0))
            .andExpect(jsonPath("$.carrinho[0].imagemProduto").value("/images/cookie-oreo.jpg"))
            .andExpect(jsonPath("$.carrinho[0].descricaoProduto").value("Cookie Oreo com Nutella"))
            .andExpect(jsonPath("$.subtotal").value(30.0));
    }

    @Test
    void getCart_shouldReturn404_whenCustomerNotFound() throws Exception {
        when(shoppingCartService.getCart("cust999")).thenThrow(new IllegalArgumentException("Cliente não encontrado: cust999"));

        mockMvc.perform(get("/shopping-cart/cust999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateCart_shouldReturn200AndUpdatedCartData() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        customer.setNome("João Silva");
        customer.setTelefone("11999999999");

        Customer.CartItem cartItem = new Customer.CartItem();
        cartItem.setProdutoID("2");
        cartItem.setQuantidade(3);
        cartItem.setPrecoProduto(15.0);
        cartItem.setImagemProduto("/images/cookie-branco.jpg");
        cartItem.setDescricaoProduto("Cookie Chocolate Branco");
        
        customer.setCarrinho(Arrays.asList(cartItem));

        when(shoppingCartService.updateCart(eq("cust1"), any())).thenReturn(customer);

        ShoppingCartDTOs.UpdateCartRequest request = new ShoppingCartDTOs.UpdateCartRequest();
        ShoppingCartDTOs.UpdateCartRequest.Item reqItem = new ShoppingCartDTOs.UpdateCartRequest.Item();
        reqItem.productSku = "2";
        reqItem.quantity = 3;
        request.items = Arrays.asList(reqItem);

        mockMvc.perform(put("/shopping-cart/cust1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("cust1"))
            .andExpect(jsonPath("$.customerId").value("cust1"))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.carrinho[0].produtoID").value("2"))
            .andExpect(jsonPath("$.carrinho[0].quantidade").value(3))
            .andExpect(jsonPath("$.carrinho[0].precoProduto").value(15.0))
            .andExpect(jsonPath("$.carrinho[0].imagemProduto").value("/images/cookie-branco.jpg"))
            .andExpect(jsonPath("$.carrinho[0].descricaoProduto").value("Cookie Chocolate Branco"))
            .andExpect(jsonPath("$.subtotal").value(45.0));
    }

    @Test
    void updateCart_shouldReturn400_whenCustomerNotFound() throws Exception {
        when(shoppingCartService.updateCart(eq("cust999"), any())).thenThrow(new IllegalArgumentException("Cliente não encontrado: cust999"));

        ShoppingCartDTOs.UpdateCartRequest request = new ShoppingCartDTOs.UpdateCartRequest();
        ShoppingCartDTOs.UpdateCartRequest.Item reqItem = new ShoppingCartDTOs.UpdateCartRequest.Item();
        reqItem.productSku = "1";
        reqItem.quantity = 2;
        request.items = Arrays.asList(reqItem);

        mockMvc.perform(put("/shopping-cart/cust999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateCartItem_shouldReturn200AndUpdatedCartData() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        customer.setNome("João Silva");
        customer.setTelefone("11999999999");

        Customer.CartItem cartItem = new Customer.CartItem();
        cartItem.setProdutoID("1");
        cartItem.setQuantidade(5);
        cartItem.setPrecoProduto(15.0);
        cartItem.setImagemProduto("/images/cookie-oreo.jpg");
        cartItem.setDescricaoProduto("Cookie Oreo com Nutella");
        
        customer.setCarrinho(Arrays.asList(cartItem));

        when(shoppingCartService.updateCartItem(eq("cust1"), any())).thenReturn(customer);

        ShoppingCartDTOs.UpdateItemRequest request = new ShoppingCartDTOs.UpdateItemRequest();
        request.productSku = "1";
        request.quantity = 5;

        mockMvc.perform(patch("/shopping-cart/cust1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("cust1"))
            .andExpect(jsonPath("$.customerId").value("cust1"))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.carrinho[0].produtoID").value("1"))
            .andExpect(jsonPath("$.carrinho[0].quantidade").value(5))
            .andExpect(jsonPath("$.carrinho[0].precoProduto").value(15.0))
            .andExpect(jsonPath("$.carrinho[0].imagemProduto").value("/images/cookie-oreo.jpg"))
            .andExpect(jsonPath("$.carrinho[0].descricaoProduto").value("Cookie Oreo com Nutella"))
            .andExpect(jsonPath("$.subtotal").value(75.0));
    }

    @Test
    void updateCartItem_shouldReturn200AndRemoveItem_whenQuantityIsZero() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        customer.setNome("João Silva");
        customer.setTelefone("11999999999");
        customer.setCarrinho(Arrays.asList()); // Carrinho vazio após remoção

        when(shoppingCartService.updateCartItem(eq("cust1"), any())).thenReturn(customer);

        ShoppingCartDTOs.UpdateItemRequest request = new ShoppingCartDTOs.UpdateItemRequest();
        request.productSku = "1";
        request.quantity = 0; // Remove item

        mockMvc.perform(patch("/shopping-cart/cust1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("cust1"))
            .andExpect(jsonPath("$.customerId").value("cust1"))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.carrinho").isEmpty())
            .andExpect(jsonPath("$.subtotal").value(0.0));
    }

    @Test
    void updateCartItem_shouldReturn400_whenCustomerNotFound() throws Exception {
        when(shoppingCartService.updateCartItem(eq("cust999"), any())).thenThrow(new IllegalArgumentException("Cliente não encontrado: cust999"));

        ShoppingCartDTOs.UpdateItemRequest request = new ShoppingCartDTOs.UpdateItemRequest();
        request.productSku = "1";
        request.quantity = 2;

        mockMvc.perform(patch("/shopping-cart/cust999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}


