package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelOrderUseCase {
    
    private final OrderRepository repository;
    
    @Transactional
    public OrderDTO execute(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));
        
        order.cancel();
        order = repository.save(order);
        
        return OrderDTO.from(order);
    }
}
