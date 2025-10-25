package com.sedocefosse.backend.service.customer;

import com.sedocefosse.backend.model.Customer;
import com.sedocefosse.backend.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer loginOrCreate(String nome, String telefone) {
        return customerRepository.findByTelefone(telefone)
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setNome(nome);
                    customer.setTelefone(telefone);
                    return customerRepository.save(customer);
                });
    }
}


