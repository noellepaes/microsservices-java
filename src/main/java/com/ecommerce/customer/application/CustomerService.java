package com.ecommerce.customer.application;

import com.ecommerce.customer.domain.model.Customer;
import com.ecommerce.customer.domain.repository.CustomerRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new BusinessException("Cliente com este email já existe");
        }
        if (customerRepository.findByCpf(customer.getCpf()).isPresent()) {
            throw new BusinessException("Cliente com este CPF já existe");
        }
        return customerRepository.save(customer);
    }
    
    @Transactional(readOnly = true)
    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    
    @Transactional
    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        Customer customer = findById(id);
        customer.setName(updatedCustomer.getName());
        customer.setEmail(updatedCustomer.getEmail());
        return customerRepository.save(customer);
    }
    
    @Transactional
    public void deactivateCustomer(UUID id) {
        Customer customer = findById(id);
        customer.deactivate();
        customerRepository.save(customer);
    }
}
