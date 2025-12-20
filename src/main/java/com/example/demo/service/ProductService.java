package com.example.pro.service;

import java.util.List;
import com.example.pro.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    void deactivateProduct(Long id);
}
