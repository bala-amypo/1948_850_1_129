package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product createProduct(Product product) {
        if (repo.findBySku(product.getSku()).isPresent())
            throw new IllegalArgumentException("SKU");

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price");

        return repo.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        return repo.save(existing);
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public void deactivateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        repo.save(p);
    }
}
