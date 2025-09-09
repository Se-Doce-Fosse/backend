package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.model.Product;
import com.sedocefosse.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminProductController.class) 
class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void deleteProduct_shouldReturnNoContent_whenProductExists() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/admin/products/1"))
                .andExpect(status().isNoContent());
    }
}