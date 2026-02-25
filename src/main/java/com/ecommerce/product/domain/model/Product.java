package com.ecommerce.product.domain.model;

import com.ecommerce.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products", schema = "product_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    // Regras de negócio no domínio
    public void decreaseStock(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        this.stock -= quantity;
    }
    
    public void increaseStock(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.stock += quantity;
    }
    
    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }
    
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }
    
    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE && stock > 0;
    }
}
