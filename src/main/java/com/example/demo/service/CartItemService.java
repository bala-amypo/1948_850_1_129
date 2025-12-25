package com.example.demo.service;

import java.util.List;
import com.example.demo.model.CartItem;

public interface CartItemService {

    CartItem addItemToCart(CartItem item);

    List<CartItem> getItemsForCart(Long cartId);
}
