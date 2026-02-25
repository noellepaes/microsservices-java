package com.ecommerce.customer.domain.model;

import com.ecommerce.shared.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers", schema = "customer_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String cpf;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status = CustomerStatus.ACTIVE;
    
    // Regras de negócio no domínio
    public void activate() {
        this.status = CustomerStatus.ACTIVE;
    }
    
    public void deactivate() {
        this.status = CustomerStatus.INACTIVE;
    }
    
    public boolean isActive() {
        return status == CustomerStatus.ACTIVE;
    }
}
