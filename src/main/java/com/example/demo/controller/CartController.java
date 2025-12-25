package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}/items")
    public Cart addItem(@PathVariable Long cartId, @RequestBody CartItem item) {
        return cartService.addItem(cartId, item);
    }
}
