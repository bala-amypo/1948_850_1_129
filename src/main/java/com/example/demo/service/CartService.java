package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;

public interface CartService {
    Cart addItem(Long cartId, CartItem item);
}
