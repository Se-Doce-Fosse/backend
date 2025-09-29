package com.sedocefosse.backend.controller;

import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

@RestController("customerLoginController")
@RequestMapping({"/login", "/login/"})
public class LoginController {

    private final CustomerService customerService;

    public LoginController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> login(@Validated @RequestBody LoginRequest request) {
        Customer customer = customerService.loginOrCreate(request.getNome(), request.getTelefone());
        return ResponseEntity.ok(customer);
    }

    public static class LoginRequest {
        @NotBlank
        private String nome;
        @NotBlank
        private String telefone;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }
    }
}


