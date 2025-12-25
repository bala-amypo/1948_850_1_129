package com.example.demo.repository;

import java.util.*;
import com.example.demo.model.*;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    Product save(Product product);
}
