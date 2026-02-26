package com.ecommerce.order.presentation;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.application.usecase.*;
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
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderDTO order = createOrderUseCase.execute(request.customerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable UUID id) {
        OrderDTO order = getOrderUseCase.findById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable UUID customerId) {
        List<OrderDTO> orders = getOrderUseCase.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDTO> addItem(
            @PathVariable UUID id,
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
    public ResponseEntity<OrderDTO> payOrder(@PathVariable UUID id) {
        OrderDTO order = payOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable UUID id) {
        OrderDTO order = cancelOrderUseCase.execute(id);
        return ResponseEntity.ok(order);
    }
}
