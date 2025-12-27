package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Cart Management", description = "APIs for shopping carts")
public class CartController {
    private final CartServiceImpl cartService;
    
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }
    
    @PostMapping("/user/{userId}")
    @Operation(summary = "Create or get active cart", description = "Creates cart or returns existing active cart")
    public ResponseEntity<Cart> createCart(@PathVariable Long userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get active cart", description = "Gets the active cart for user")
    public ResponseEntity<Cart> getActiveCart(@PathVariable Long userId) {
        Cart cart = cartService.getActiveCartForUser(userId);
        return ResponseEntity.ok(cart);
    }
}