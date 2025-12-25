package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/{userId}")
    public Cart createCart(@PathVariable Long userId) {
        return service.createCart(userId);
    }

    @GetMapping("/active/{userId}")
    public Cart getActiveCart(@PathVariable Long userId) {
        return service.getActiveCartForUser(userId);
    }
}
