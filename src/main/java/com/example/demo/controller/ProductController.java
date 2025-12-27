package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final ProductServiceImpl productService;
    
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with unique SKU")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates product name and price")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by ID")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate product", description = "Marks product as inactive")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok().build();
    }
}