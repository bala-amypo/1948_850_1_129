package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart addItem(Long cartId, CartItem item) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // ⭐ REQUIRED TESTCASE
        if (!cart.isActive()) {
            throw new IllegalArgumentException("Inactive cart cannot accept items");
        }

        item.setCart(cart);
        cart.getItems().add(item);

        return cartRepository.save(cart);
    }
}
