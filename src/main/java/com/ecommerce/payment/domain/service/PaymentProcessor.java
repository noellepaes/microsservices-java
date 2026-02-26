package com.ecommerce.payment.domain.service;

import com.ecommerce.payment.domain.model.Payment;

public interface PaymentProcessor {
    void process(Payment payment);
}
