package com.sedocefosse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.service.customer.CustomerService;
import com.sedocefosse.backend.configs.security.TokenService;
import com.sedocefosse.backend.repository.admin.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AdminRepository adminRepository;

    @Test
    void login_shouldReturnExistingCustomer_orCreateAndReturn() throws Exception {
        Customer existing = new Customer();
        existing.setId("123");
        existing.setNome("Alice Smith");
        existing.setTelefone("987654321");

        when(customerService.loginOrCreate(anyString(), anyString())).thenReturn(existing);

        String body = "{\"nome\":\"Alice Smith\",\"telefone\":\"987654321\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.nome").value("Alice Smith"))
                .andExpect(jsonPath("$.telefone").value("987654321"));
    }
}


