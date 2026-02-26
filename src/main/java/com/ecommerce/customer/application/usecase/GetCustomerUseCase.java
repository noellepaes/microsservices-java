package com.ecommerce.customer.application.usecase;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCustomerUseCase {
    
    private final CustomerRepository repository;
    
    @Transactional(readOnly = true)
    public CustomerDTO findById(UUID id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        return CustomerDTO.from(customer);
    }
    
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        return repository.findAll().stream()
                .map(CustomerDTO::from)
                .collect(Collectors.toList());
    }
}
