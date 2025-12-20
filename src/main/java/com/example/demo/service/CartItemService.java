package com.example.pro.service;

import java.util.List;
import com.example.pro.entity.CartItem;

public interface CartItemService {

    CartItem addItem(CartItem item);

    CartItem updateItem(Long id, CartItem item);

    CartItem getItemById(Long id);

    List<CartItem> getAllItems();

    void removeItem(Long id);
}
