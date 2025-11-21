package com.sedocefosse.backend.repository.customer;

import com.sedocefosse.backend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByTelefone(String telefone);
}


