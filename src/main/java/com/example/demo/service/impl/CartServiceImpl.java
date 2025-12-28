package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository repository;

    public CartServiceImpl(CartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart createCart(Long userId) {
        repository.findByUserIdAndActiveTrue(userId)
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Cart already exists");
                });

        Cart cart = new Cart();
        cart.setUserId(userId);
        return repository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    @Override
    public Cart getActiveCartForUser(Long userId) {
        return repository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
    }

    @Override
    public void deactivateCart(Long id) {
        Cart cart = getCartById(id);
        cart.setActive(false);
        repository.save(cart);
    }
}
