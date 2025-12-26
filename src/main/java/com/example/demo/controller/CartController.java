// CartController.java
package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping("/user/{userId}")
    public Cart create(@PathVariable Long userId) {
        return service.createCart(userId);
    }

    @GetMapping("/user/{userId}")
    public Cart getActive(@PathVariable Long userId) {
        return service.getActiveCartForUser(userId);
    }
}
