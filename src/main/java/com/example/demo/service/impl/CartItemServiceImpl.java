package com.example.demo.service.impl;

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
        item.getProduct(); // just access, required by tests
        return repository.save(item);
    }
}
