package com.ecommerce.customer.domain.exception;

import com.ecommerce.shared.exception.BusinessException;

public class CustomerException extends BusinessException {
    public CustomerException(String message) {
        super(message);
    }
    
    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
