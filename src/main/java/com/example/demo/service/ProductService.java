package com.example.demo.service;

import com.example.demo.model.Product;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product getProductById(Long id);

    void deactivateProduct(Long id);
}
