package com.ecommerce.payment.infrastructure.repository;

import com.ecommerce.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, UUID> {
    
    List<Payment> findByOrderId(UUID orderId);
}
