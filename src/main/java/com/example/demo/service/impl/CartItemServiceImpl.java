// CartItemServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository itemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(CartItemRepository i, CartRepository c, ProductRepository p) {
        this.itemRepo = i;
        this.cartRepo = c;
        this.productRepo = p;
    }

    public CartItem addItemToCart(CartItem item) {
        Cart cart = cartRepo.findById(item.getCart().getId()).orElseThrow();
        if (!cart.getActive())
            throw new IllegalArgumentException("active carts");
        if (item.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity");
        Product product = productRepo.findById(item.getProduct().getId()).orElseThrow();

        return itemRepo.findByCartIdAndProductId(cart.getId(), product.getId())
                .map(ex -> {
                    ex.setQuantity(ex.getQuantity() + item.getQuantity());
                    return itemRepo.save(ex);
                })
                .orElseGet(() -> itemRepo.save(item));
    }

    public List<CartItem> getItemsForCart(Long cartId) {
        return itemRepo.findByCartId(cartId);
    }
}
