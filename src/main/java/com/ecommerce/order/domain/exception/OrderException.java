package com.ecommerce.order.domain.exception;

import com.ecommerce.shared.exception.BusinessException;

public class OrderException extends BusinessException {
    public OrderException(String message) {
        super(message);
    }
    
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
