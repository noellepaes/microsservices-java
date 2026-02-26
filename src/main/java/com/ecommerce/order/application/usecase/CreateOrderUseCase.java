package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
    
    private final OrderRepository repository;
    
    @Transactional
    public OrderDTO execute(UUID customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order = repository.save(order);
        return OrderDTO.from(order);
    }
}
