package com.example.pro.service;

import java.util.List;
import com.example.pro.entity.Cart;

public interface CartService {

    Cart createCart(Cart cart);

    Cart updateCart(Long id, Cart cart);

    Cart getCartById(Long id);

    List<Cart> getAllCarts();

    void deleteCart(Long id);
}
