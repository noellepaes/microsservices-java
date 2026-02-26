package com.ecommerce.order.application.usecase;

import com.ecommerce.order.application.dto.OrderDTO;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddItemToOrderUseCase {
    
    private final OrderRepository repository;
    
    @Transactional
    public OrderDTO execute(UUID orderId, UUID productId, String productName, Integer quantity, BigDecimal unitPrice) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));
        
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setProductName(productName);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        
        order.addItem(item);
        order = repository.save(order);
        
        return OrderDTO.from(order);
    }
}
