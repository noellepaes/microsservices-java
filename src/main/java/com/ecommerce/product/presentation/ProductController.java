package com.ecommerce.product.presentation;

import com.ecommerce.product.application.dto.ProductDTO;
import com.ecommerce.product.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "API para gerenciamento de produtos")
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DecreaseStockUseCase decreaseStockUseCase;
    
    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductDTO productDTO = new ProductDTO(
                null, request.name(), request.description(), 
                request.price(), request.stock(), null, null, null
        );
        ProductDTO created = createProductUseCase.execute(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductDTO> getProduct(
            @Parameter(description = "ID do produto", required = true) @PathVariable UUID id) {
        ProductDTO product = getProductUseCase.findById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = getProductUseCase.findAll();
        return ResponseEntity.ok(products);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID do produto", required = true) @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        ProductDTO productDTO = new ProductDTO(
                null, request.name(), request.description(), 
                request.price(), null, null, null, null
        );
        ProductDTO updated = updateProductUseCase.execute(id, productDTO);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/{id}/decrease-stock")
    @Operation(summary = "Diminuir estoque", description = "Reduz a quantidade em estoque de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quantidade inválida ou estoque insuficiente"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductDTO> decreaseStock(
            @Parameter(description = "ID do produto", required = true) @PathVariable UUID id,
            @Parameter(description = "Quantidade a diminuir", required = true) @RequestParam Integer quantity) {
        ProductDTO product = decreaseStockUseCase.execute(id, quantity);
        return ResponseEntity.ok(product);
    }
}
