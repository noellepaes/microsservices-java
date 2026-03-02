package com.ecommerce.customer.presentation;

import com.ecommerce.customer.application.dto.CustomerDTO;
import com.ecommerce.customer.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customers", description = "API para gerenciamento de clientes")
public class CustomerController {
    
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    
    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = new CustomerDTO(
                null, request.name(), request.email(), 
                request.cpf(), null, null, null
        );
        CustomerDTO created = createCustomerUseCase.execute(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerDTO> getCustomer(
            @Parameter(description = "ID do cliente", required = true) @PathVariable UUID id) {
        CustomerDTO customer = getCustomerUseCase.findById(id);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = getCustomerUseCase.findAll();
        return ResponseEntity.ok(customers);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID do cliente", required = true) @PathVariable UUID id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerDTO customerDTO = new CustomerDTO(
                null, request.name(), request.email(), 
                null, null, null, null
        );
        CustomerDTO updated = updateCustomerUseCase.execute(id, customerDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar cliente", description = "Desativa um cliente (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerDTO> deactivateCustomer(
            @Parameter(description = "ID do cliente", required = true) @PathVariable UUID id) {
        CustomerDTO customer = deactivateCustomerUseCase.execute(id);
        return ResponseEntity.ok(customer);
    }
}
