package com.ecommerce.product.presentation;

import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.application.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DecreaseStockUseCase decreaseStockUseCase;
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductDTO productDTO = new ProductDTO(
                null, request.name(), request.description(), 
                request.price(), request.stock(), null, null, null
        );
        ProductDTO created = createProductUseCase.execute(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id) {
        ProductDTO product = getProductUseCase.findById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = getProductUseCase.findAll();
        return ResponseEntity.ok(products);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        ProductDTO productDTO = new ProductDTO(
                null, request.name(), request.description(), 
                request.price(), null, null, null, null
        );
        ProductDTO updated = updateProductUseCase.execute(id, productDTO);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/{id}/decrease-stock")
    public ResponseEntity<ProductDTO> decreaseStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        ProductDTO product = decreaseStockUseCase.execute(id, quantity);
        return ResponseEntity.ok(product);
    }
}
