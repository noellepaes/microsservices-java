package com.ecommerce.payment.application.usecase;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentMethod;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.payment.domain.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {
    
    private final PaymentRepository repository;
    private final PaymentProcessor processor;
    
    @Transactional
    public void execute(UUID orderId, BigDecimal amount, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setMethod(method);
        
        processor.process(payment);
        
        repository.save(payment);
    }
}
