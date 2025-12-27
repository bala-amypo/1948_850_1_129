package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    @Override
    @Transactional
    public Cart createCart(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndActiveTrue(userId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setActive(true);
        return cartRepository.save(cart);
    }
    
    @Override
    public Cart getActiveCartForUser(Long userId) {
        return cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Active cart not found"));
    }
}
