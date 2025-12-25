package com.example.demo.service;

import java.util.List;
import com.example.demo.model.CartItem;

public interface CartItemService {

    CartItem save(CartItem item);

    // ⭐ REQUIRED
    List<CartItem> getItemsForCart(Long cartId);
}
