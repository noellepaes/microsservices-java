package com.ecommerce.customer.presentation;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.application.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = new CustomerDTO(
                null, request.name(), request.email(), 
                request.cpf(), null, null, null
        );
        CustomerDTO created = createCustomerUseCase.execute(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable UUID id) {
        CustomerDTO customer = getCustomerUseCase.findById(id);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = getCustomerUseCase.findAll();
        return ResponseEntity.ok(customers);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = new CustomerDTO(
                null, request.name(), request.email(), 
                null, null, null, null
        );
        CustomerDTO updated = updateCustomerUseCase.execute(id, customerDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDTO> deactivateCustomer(@PathVariable UUID id) {
        CustomerDTO customer = deactivateCustomerUseCase.execute(id);
        return ResponseEntity.ok(customer);
    }
}
