package com.ecommerce.customer.infrastructure.persistence;

import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCustomerRepository extends JpaRepository<Customer, UUID>, CustomerRepository {
    
    @Override
    Optional<Customer> findByEmail(String email);
    
    @Override
    Optional<Customer> findByCpf(String cpf);
}
