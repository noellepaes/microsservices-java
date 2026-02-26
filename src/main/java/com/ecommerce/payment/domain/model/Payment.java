package com.ecommerce.payment.domain.model;

import com.ecommerce.payment.domain.exception.PaymentFailedException;
import com.ecommerce.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments", schema = "payment_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    
    @Column(nullable = false)
    private UUID orderId;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;
    
    // Regras de negócio no domínio
    public void approve() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentFailedException("Pagamento já processado");
        }
        this.status = PaymentStatus.APPROVED;
    }
    
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
    
    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }
    
    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }
}
