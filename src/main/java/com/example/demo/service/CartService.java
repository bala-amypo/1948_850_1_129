package com.example.demo.service;

import com.example.demo.model.Cart;

public interface CartService {

    Cart createCart(Long userId);

    Cart getActiveCartForUser(Long userId);
}
