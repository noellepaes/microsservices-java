package com.ecommerce.order.domain.model;

import com.ecommerce.order.domain.exception.OrderException;
import com.ecommerce.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "order_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    
    @Column(nullable = false)
    private UUID customerId;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    // Regras de negócio no domínio - Aggregate Root
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
        calculateTotal();
    }
    
    public void removeItem(OrderItem item) {
        this.items.remove(item);
        item.setOrder(null);
        calculateTotal();
    }
    
    private void calculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Regra: Só pode ir de PENDING → PAID
    // Nunca pode ir de CANCELLED → PAID
    public void pay() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderException(
                String.format("Pedido não pode ser pago. Status atual: %s. Apenas pedidos PENDING podem ser pagos.", this.status)
            );
        }
        this.status = OrderStatus.PAID;
    }
    
    public void cancel() {
        if (this.status == OrderStatus.PAID) {
            throw new OrderException("Pedido pago não pode ser cancelado");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderException("Pedido já está cancelado");
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderException("Apenas pedidos PENDING podem ser confirmados");
        }
        this.status = OrderStatus.CONFIRMED;
    }
}
