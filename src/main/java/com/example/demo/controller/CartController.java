package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/carts")
@SecurityRequirement(name="bearerAuth")

public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Cart> createCart(@PathVariable Long userId) {
        return ResponseEntity.ok(service.createCart(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getActiveCartForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCartById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateCart(@PathVariable Long id) {
        service.deactivateCart(id);
        return ResponseEntity.ok("Cart deactivated successfully");
    }
}
