package com.example.demo.service;

import com.example.demo.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    void deactivateProduct(Long id);
}
