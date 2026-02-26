package com.ecommerce.customer.application.usecase;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {
    
    private final CustomerRepository repository;
    
    @Transactional
    public CustomerDTO execute(UUID id, CustomerDTO customerDTO) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        
        customer.setName(customerDTO.name());
        customer.setEmail(customerDTO.email());
        
        customer = repository.save(customer);
        return CustomerDTO.from(customer);
    }
}
