package com.ecommerce.customer.domain.repository;

import com.ecommerce.customer.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
    List<Customer> findAll();
    void deleteById(UUID id);
}
