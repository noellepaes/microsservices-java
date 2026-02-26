package com.ecommerce.product.domain.exception;

import com.ecommerce.shared.exception.BusinessException;

public class ProductException extends BusinessException {
    public ProductException(String message) {
        super(message);
    }
    
    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
