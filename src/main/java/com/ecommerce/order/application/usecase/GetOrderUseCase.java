package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrderUseCase {
    
    private final OrderRepository repository;
    
    @Transactional(readOnly = true)
    public OrderDTO findById(UUID id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));
        return OrderDTO.from(order);
    }
    
    @Transactional(readOnly = true)
    public List<OrderDTO> findByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(OrderDTO::from)
                .collect(Collectors.toList());
    }
}
