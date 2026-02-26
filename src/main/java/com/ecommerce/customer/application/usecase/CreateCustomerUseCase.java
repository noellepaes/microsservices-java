package com.ecommerce.customer.application.usecase;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {
    
    private final CustomerRepository repository;
    
    @Transactional
    public CustomerDTO execute(CustomerDTO customerDTO) {
        if (repository.findByEmail(customerDTO.email()).isPresent()) {
            throw new BusinessException("Cliente com este email já existe");
        }
        if (repository.findByCpf(customerDTO.cpf()).isPresent()) {
            throw new BusinessException("Cliente com este CPF já existe");
        }
        
        Customer customer = new Customer();
        customer.setName(customerDTO.name());
        customer.setEmail(customerDTO.email());
        customer.setCpf(customerDTO.cpf());
        
        customer = repository.save(customer);
        return CustomerDTO.from(customer);
    }
}
