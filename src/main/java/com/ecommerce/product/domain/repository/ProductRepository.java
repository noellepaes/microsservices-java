package com.ecommerce.product.domain.repository;

import com.ecommerce.product.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    List<Product> findByStatus(com.ecommerce.product.domain.model.ProductStatus status);
    void deleteById(UUID id);
}
