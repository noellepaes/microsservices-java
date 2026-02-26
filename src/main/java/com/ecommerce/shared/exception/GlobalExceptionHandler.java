package com.ecommerce.shared.exception;

import com.ecommerce.customer.domain.exception.CustomerException;
import com.ecommerce.order.domain.exception.OrderException;
import com.ecommerce.payment.domain.exception.PaymentFailedException;
import com.ecommerce.product.domain.exception.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Business Rule Violation", ex.getMessage());
    }
    
    @ExceptionHandler({OrderException.class, PaymentFailedException.class, ProductException.class, CustomerException.class})
    public ResponseEntity<Map<String, Object>> handleDomainException(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Domain Rule Violation", ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        
        return ResponseEntity.status(status).body(body);
    }
}
