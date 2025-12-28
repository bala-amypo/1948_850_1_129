package com.example.demo.service;

import com.example.demo.model.CartItem;
import java.util.List;

public interface CartItemService {

    CartItem addItemToCart(CartItem item);

    CartItem updateItem(Long id, Integer quantity);

    List<CartItem> getItemsForCart(Long cartId);

    void removeItem(Long id);
}
