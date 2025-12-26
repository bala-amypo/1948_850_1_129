package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        productRepository.findBySku(product.getSku())
                .ifPresent(p -> {
                    throw new IllegalArgumentException("SKU already exists");
                });

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());

        return productRepository.save(existing);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }
}
