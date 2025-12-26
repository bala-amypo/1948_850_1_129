package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartItemServiceImpl(CartItemRepository cartItemRepo,
                               CartRepository cartRepo,
                               ProductRepository productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @Override
    public CartItem addItemToCart(CartItem item) {
        // Validate input
        if (item.getCart() == null || item.getCart().getId() == null)
            throw new IllegalArgumentException("Cart ID is required");
        if (item.getProduct() == null || item.getProduct().getId() == null)
            throw new IllegalArgumentException("Product ID is required");
        if (item.getQuantity() == null || item.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity must be positive");

        // Fetch cart
        Cart cart = cartRepo.findById(item.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive())
            throw new IllegalArgumentException("Cannot add items to inactive cart");

        // Fetch product
        Product product = productRepo.findById(item.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!product.getActive())
            throw new IllegalArgumentException("Cannot add inactive product to cart");

        // Check if item already exists
        CartItem existing = cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            return cartItemRepo.save(existing);
        }

        // Save new cart item
        item.setCart(cart);
        item.setProduct(product);
        return cartItemRepo.save(item);
    }

    @Override
    public List<CartItem> getItemsForCart(Long cartId) {
        return cartItemRepo.findByCartId(cartId);
    }
}
