package com.example.demo.repository;

import java.util.Optional;
import com.example.demo.model.Cart;

public interface CartRepository {
    Optional<Cart> findById(Long id);
    Optional<Cart> findByUserIdAndActiveTrue(Long userId);
    Cart save(Cart cart);
}
