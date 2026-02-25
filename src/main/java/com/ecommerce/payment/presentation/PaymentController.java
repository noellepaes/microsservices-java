package com.ecommerce.payment.presentation;

import com.ecommerce.payment.application.PaymentService;
import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @RequestParam UUID orderId,
            @RequestParam BigDecimal amount,
            @RequestParam PaymentMethod method) {
        Payment payment = paymentService.createPayment(orderId, amount, method);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable UUID id) {
        Payment payment = paymentService.findById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrder(@PathVariable UUID orderId) {
        List<Payment> payments = paymentService.findByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<Payment> approvePayment(@PathVariable UUID id) {
        Payment payment = paymentService.approvePayment(id);
        return ResponseEntity.ok(payment);
    }
    
    @PostMapping("/{id}/reject")
    public ResponseEntity<Payment> rejectPayment(@PathVariable UUID id) {
        Payment payment = paymentService.rejectPayment(id);
        return ResponseEntity.ok(payment);
    }
}
