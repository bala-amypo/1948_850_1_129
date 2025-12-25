package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem addItemToCart(CartItem item) {
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Optional<CartItem> existing = cartItemRepository
                .findByCartIdAndProductId(
                        item.getCart().getId(),
                        item.getProduct().getId()
                );

        if (existing.isPresent()) {
            CartItem updated = existing.get();
            updated.setQuantity(updated.getQuantity() + item.getQuantity());
            return cartItemRepository.save(updated);
        }

        return cartItemRepository.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }
}
