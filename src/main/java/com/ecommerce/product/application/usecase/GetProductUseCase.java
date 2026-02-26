package com.ecommerce.product.application.usecase;

import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetProductUseCase {
    
    private final ProductRepository repository;
    
    @Transactional(readOnly = true)
    public ProductDTO findById(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));
        return ProductDTO.from(product);
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(ProductDTO::from)
                .collect(Collectors.toList());
    }
}
