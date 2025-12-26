package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Cart> create(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.createCart(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getActive(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getActiveCartForUser(userId));
    }
}
