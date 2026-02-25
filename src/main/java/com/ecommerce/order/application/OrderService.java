package com.ecommerce.order.application;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    @Transactional
    public Order createOrder(UUID customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        return orderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    @Transactional
    public Order addItem(UUID orderId, UUID productId, String productName, Integer quantity, BigDecimal unitPrice) {
        Order order = findById(orderId);
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setProductName(productName);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        order.addItem(item);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order payOrder(UUID orderId) {
        Order order = findById(orderId);
        order.pay();
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = findById(orderId);
        order.cancel();
        return orderRepository.save(order);
    }
}
