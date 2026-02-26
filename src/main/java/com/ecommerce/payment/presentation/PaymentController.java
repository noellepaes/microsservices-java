package com.ecommerce.payment.presentation;

import com.ecommerce.payment.application.dto.PaymentDTO;
import com.ecommerce.payment.application.usecase.GetPaymentUseCase;
import com.ecommerce.payment.application.usecase.ProcessPaymentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    
    @PostMapping
    public ResponseEntity<Void> pay(@Valid @RequestBody PaymentRequest request) {
        processPaymentUseCase.execute(request.orderId(), request.amount(), request.method());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable UUID id) {
        PaymentDTO payment = getPaymentUseCase.findById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrder(@PathVariable UUID orderId) {
        List<PaymentDTO> payments = getPaymentUseCase.findByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
}
