package com.sedocefosse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedocefosse.backend.configs.security.TokenService;
import com.sedocefosse.backend.model.Category;
import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.repository.AdminRepository;
import com.sedocefosse.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.sedocefosse.backend.controller.admin.AdminProductController;

import java.math.BigDecimal;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AdminProductController.class)
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AdminRepository adminRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product createTestProduct(String sku, String nome, String descricao, BigDecimal valor, String imagemUrl, Boolean ativo, Category categoria) {
        return new Product(sku, nome, descricao, valor, imagemUrl, ativo, categoria);
    }

    private Product createDefaultTestProduct(String sku, String nome, BigDecimal valor, Boolean ativo) {
        Category defaultCategory = new Category();
        defaultCategory.setId(1L);
        defaultCategory.setNome("Default Category");
        defaultCategory.setProdutos(null); 

        return createTestProduct(sku, nome, "Descrição padrão para " + nome, valor, "http://example.com/images/" + sku + ".jpg", ativo, defaultCategory);
    }


    @Test
    void deleteProduct_shouldReturnNoContent_whenProductExists() throws Exception {
        String productSku = "SKU-CHOCO-01";

        doNothing().when(productService).deleteProductById(productSku);

        mockMvc.perform(delete("/admin/products/" + productSku)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        String newSku = "NEW-PRODUCT-SKU";

        Product newProductRequest = createDefaultTestProduct(newSku, "New Product", BigDecimal.valueOf(10.00), true);
        Product createdProduct = createDefaultTestProduct(newSku, "New Product", BigDecimal.valueOf(10.00), true);

        when(productService.create(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/admin/products")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value(newSku))
                .andExpect(jsonPath("$.nome").value("New Product"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void toggleProductStatus_shouldReturnUpdatedProduct_whenProductExists() throws Exception {
        String productSku = "existing-product-sku";

        Product existingProduct = createDefaultTestProduct(productSku, "Existing Product", BigDecimal.valueOf(5.00), true);
        Product updatedProduct = createDefaultTestProduct(productSku, "Existing Product", BigDecimal.valueOf(5.00), false);

        when(productService.toggleStatus(productSku)).thenReturn(updatedProduct);

        mockMvc.perform(patch("/admin/products/" + productSku + "/status")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    void toggleProductStatus_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
        String productSku = "non-existent-product-sku";

        when(productService.toggleStatus(productSku)).thenReturn(null);

        mockMvc.perform(patch("/admin/products/" + productSku + "/status")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}