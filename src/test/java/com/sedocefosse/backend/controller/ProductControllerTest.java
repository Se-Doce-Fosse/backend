package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.model.Category;
import com.sedocefosse.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.sedocefosse.backend.dto.CategoryDTO;
import com.sedocefosse.backend.dto.ProductDTO;
import com.sedocefosse.backend.dto.ProductDetailsDTO;
import com.sedocefosse.backend.dto.RelatedProductDTO;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProductBySku_shouldReturnProduct_whenSkuExists() throws Exception {
        Category mockCategoria = new Category();
        mockCategoria.setId(1L);
        mockCategoria.setNome("Doces");

        ProductDetailsDTO mockProductDetails = new ProductDetailsDTO();
        mockProductDetails.setSku("SKU-CHOCO-01");
        mockProductDetails.setNome("Cookie de Chocolate");
        mockProductDetails.setValor(new BigDecimal("5.50"));

        when(productService.findProductDetailsBySku("SKU-CHOCO-01")).thenReturn(Optional.of(mockProductDetails));

        mockMvc.perform(get("/products/SKU-CHOCO-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-CHOCO-01"))
                .andExpect(jsonPath("$.nome").value("Cookie de Chocolate"))
                .andExpect(jsonPath("$.valor").value(5.50));
    }

    @Test
    void getProductBySku_shouldReturnNotFound_whenSkuDoesNotExist() throws Exception {
        when(productService.findProductDetailsBySku("SKU-INVALIDO")).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/SKU-INVALIDO"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_shouldReturnGroupedProducts_whenCalled() throws Exception {
        RelatedProductDTO relatedCookieBranco = new RelatedProductDTO("2", "Cookie Chocolate Branco", "R$ 15,00", "/images/cookie-branco.jpg", "Cookie Chocolate Branco");
        RelatedProductDTO relatedBolo = new RelatedProductDTO("10", "Bolo Red Velvet", "R$ 35,00", "/images/bolo-red-velvet.jpg", "Bolo Red Velvet");

        ProductDTO cookieOreo = new ProductDTO(
            "1", "Cookie Oreo com Nutella", "R$ 15,00", "/images/cookie-oreo.jpg",
            "Cookie Oreo com Nutella", Arrays.asList("Sem Glúten", "Sem Lactose", "Vegan"),
            Arrays.asList(relatedCookieBranco, relatedBolo)
        );

        ProductDTO boloRedVelvet = new ProductDTO(
            "10", "Bolo Red Velvet", "R$ 35,00", "/images/bolo-red-velvet.jpg",
            "Bolo Red Velvet", Arrays.asList("Sem Glúten", "Sem Lactose", "Vegan"),
            Arrays.asList(new RelatedProductDTO("1", "Cookie Oreo com Nutella", "R$ 15,00", "/images/cookie-oreo.jpg", "Cookie Oreo com Nutella"))
        );

        CategoryDTO cookiesCategory = new CategoryDTO("cookies", "Cookies", Arrays.asList(cookieOreo));
        CategoryDTO bolosCategory = new CategoryDTO("bolos", "Bolos", Arrays.asList(boloRedVelvet));

        List<CategoryDTO> mockCategories = Arrays.asList(cookiesCategory, bolosCategory);

        when(productService.getAllProductsGroupedByCategory()).thenReturn(mockCategories);

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.categories").isArray())
            .andExpect(jsonPath("$.categories.length()").value(2))
            .andExpect(jsonPath("$.categories[0].id").value("cookies"))
            .andExpect(jsonPath("$.categories[0].name").value("Cookies"))
            .andExpect(jsonPath("$.categories[0].products.length()").value(1))
            .andExpect(jsonPath("$.categories[0].products[0].name").value("Cookie Oreo com Nutella"))
            .andExpect(jsonPath("$.categories[0].products[0].price").value("R$ 15,00"))
            .andExpect(jsonPath("$.categories[0].products[0].relatedProducts.length()").value(2))
            .andExpect(jsonPath("$.categories[1].name").value("Bolos"))
            .andExpect(jsonPath("$.categories[1].products[0].name").value("Bolo Red Velvet"));
    }
}