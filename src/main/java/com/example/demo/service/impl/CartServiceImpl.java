package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import jakarta.persistence.EntityNotFoundException;

@Service

public class CartServiceImpl implements CartService {

    private final CartRepository repo;

    public CartServiceImpl(CartRepository repo) {
        this.repo = repo;
    }

    @Override
    public Cart createCart(Long userId) {
        return repo.findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(userId);
                    c.setActive(true);
                    return repo.save(c);
                });
    }

    @Override
    public Cart getActiveCartForUser(Long userId) {
        return repo.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
    }
}
