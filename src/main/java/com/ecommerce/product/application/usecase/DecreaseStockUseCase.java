package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DecreaseStockUseCase {
    
    private final ProductRepository repository;
    
    @Transactional
    public ProductDTO execute(UUID id, Integer quantity) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));
        
        product.decreaseStock(quantity);
        product = repository.save(product);
        
        return ProductDTO.from(product);
    }
}
