package com.ecommerce.order.presentation;

import com.ecommerce.order.application.OrderService;
import com.ecommerce.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam UUID customerId) {
        Order order = orderService.createOrder(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable UUID customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/{id}/items")
    public ResponseEntity<Order> addItem(
            @PathVariable UUID id,
            @RequestParam UUID productId,
            @RequestParam String productName,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal unitPrice) {
        Order order = orderService.addItem(id, productId, productName, quantity, unitPrice);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> payOrder(@PathVariable UUID id) {
        Order order = orderService.payOrder(id);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable UUID id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }
}
