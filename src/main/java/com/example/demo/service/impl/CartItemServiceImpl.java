package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository itemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(CartItemRepository i, CartRepository c, ProductRepository p) {
        this.itemRepo = i;
        this.cartRepo = c;
        this.productRepo = p;
    }

    @Override
    public CartItem addItemToCart(CartItem item) {
        if (item.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity");

        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive())
            throw new IllegalArgumentException("active carts");

        Product product = productRepo.findById(item.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return itemRepo.findByCartIdAndProductId(cart.getId(), product.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return itemRepo.save(existing);
                })
                .orElseGet(() -> {
                    item.setCart(cart);
                    item.setProduct(product);
                    return itemRepo.save(item);
                });
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return itemRepo.findByCartId(cartId);
    }
}
