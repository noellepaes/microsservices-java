package com.ecommerce.product.application;

import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new BusinessException("Produto com este nome já existe");
        }
        return productRepository.save(product);
    }
    
    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    @Transactional
    public Product updateProduct(UUID id, Product updatedProduct) {
        Product product = findById(id);
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        return productRepository.save(product);
    }
    
    @Transactional
    public void decreaseStock(UUID id, Integer quantity) {
        Product product = findById(id);
        product.decreaseStock(quantity);
        productRepository.save(product);
    }
}
