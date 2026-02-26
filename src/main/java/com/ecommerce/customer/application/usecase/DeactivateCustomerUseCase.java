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
public class DeactivateCustomerUseCase {
    
    private final CustomerRepository repository;
    
    @Transactional
    public CustomerDTO execute(UUID id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        
        customer.deactivate();
        customer = repository.save(customer);
        
        return CustomerDTO.from(customer);
    }
}
