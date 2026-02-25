package com.ecommerce.payment.application;

import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentMethod;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public Payment createPayment(UUID orderId, BigDecimal amount, PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setMethod(method);
        return paymentRepository.save(payment);
    }
    
    @Transactional(readOnly = true)
    public Payment findById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public List<Payment> findByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    @Transactional
    public Payment approvePayment(UUID id) {
        Payment payment = findById(id);
        payment.approve();
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Payment rejectPayment(UUID id) {
        Payment payment = findById(id);
        payment.reject();
        return paymentRepository.save(payment);
    }
}
