package com.ecommerce.order.presentation;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.application.usecase.*;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API para gerenciamento de pedidos")
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    
    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido para um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderDTO order = createOrderUseCase.execute(request.customerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> getOrder(
            @Parameter(description = "ID do pedido", required = true) @PathVariable UUID id) {
        OrderDTO order = getOrderUseCase.findById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(
            @Parameter(description = "ID do cliente", required = true) @PathVariable UUID customerId) {
        List<OrderDTO> orders = getOrderUseCase.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/{id}/items")
    @Operation(summary = "Adicionar item ao pedido", description = "Adiciona um produto ao pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> addItem(
            @Parameter(description = "ID do pedido", required = true) @PathVariable UUID id,
            @Valid @RequestBody AddItemRequest request) {
        OrderDTO order = addItemToOrderUseCase.execute(
                id,
                request.productId(),
                request.productName(),
                request.quantity(),
                request.unitPrice()
        );
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/pay")
    @Operation(summary = "Pagar pedido", description = "Marca um pedido como pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido pago com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido não pode ser pago"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> payOrder(
            @Parameter(description = "ID do pedido", required = true) @PathVariable UUID id) {
        OrderDTO order = payOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "ID do pedido", required = true) @PathVariable UUID id) {
        OrderDTO order = cancelOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }
}
