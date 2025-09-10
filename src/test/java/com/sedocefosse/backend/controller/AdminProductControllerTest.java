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
    
        String productSku = "SKU-CHOCO-01";
        
        doNothing().when(productService).deleteProductById(productSku);

        mockMvc.perform(delete("/admin/products/" + productSku))
                .andExpect(status().isNoContent()); 
    }

}