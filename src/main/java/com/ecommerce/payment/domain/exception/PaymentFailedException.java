package com.ecommerce.payment.domain.exception;

import com.ecommerce.shared.exception.BusinessException;

public class PaymentFailedException extends BusinessException {
    public PaymentFailedException(String message) {
        super(message);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
