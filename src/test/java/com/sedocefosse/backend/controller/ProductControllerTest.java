package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProductById_shouldReturnProduct_whenIdExists() throws Exception {
        Product mockProduct = new Product(
            "SKU-CHOCO-01",
            "Cookie de Chocolate",
            "Delicioso cookie com gotas de chocolate.",
            new BigDecimal("5.50"),
            "http://example.com/cookie.jpg",
            true
        );
        mockProduct.setId(1L);

        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id").value(1)) 
                .andExpect(jsonPath("$.nome").value("Cookie de Chocolate")) 
                .andExpect(jsonPath("$.valor").value(5.50)); 
    }

    @Test
    void getProductById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        
        when(productService.findProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound()); 
    }
}