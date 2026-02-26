package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {
    
    private final ProductRepository repository;
    
    @Transactional
    public ProductDTO execute(ProductDTO productDTO) {
        if (repository.findByName(productDTO.name()).isPresent()) {
            throw new BusinessException("Produto com este nome já existe");
        }
        
        Product product = new Product();
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setStock(productDTO.stock());
        
        product = repository.save(product);
        return ProductDTO.from(product);
    }
}
