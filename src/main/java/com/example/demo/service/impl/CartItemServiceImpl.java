package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository repository;

    public CartItemServiceImpl(CartItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public CartItem save(CartItem item) {
        return repository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return repository.findByCartId(cartId);
    }
}
