package com.example.demo.service;

import com.example.demo.model.Cart;

public interface CartService {

    Cart createCart(Long userId);

    Cart getCartById(Long id);

    Cart getActiveCartForUser(Long userId);

    void deactivateCart(Long id);
}
