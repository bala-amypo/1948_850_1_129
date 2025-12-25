package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

public interface CartService {
    Cart addItem(Long cartId, CartItem item);
}
