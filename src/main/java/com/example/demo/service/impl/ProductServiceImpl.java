// ProductServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    public Product createProduct(Product p) {
        if (p.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
        if (repo.findBySku(p.getSku()).isPresent())
            throw new IllegalArgumentException("SKU already exists");
        return repo.save(p);
    }

    public Product updateProduct(Long id, Product p) {
        Product ex = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        ex.setName(p.getName());
        ex.setPrice(p.getPrice());
        return repo.save(ex);
    }

    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public void deactivateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        repo.save(p);
    }
}
