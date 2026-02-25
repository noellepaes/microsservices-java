package com.ecommerce.payment.domain.repository;

import com.ecommerce.payment.domain.model.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    List<Payment> findByOrderId(UUID orderId);
    List<Payment> findAll();
}
