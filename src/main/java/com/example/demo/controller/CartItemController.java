package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItem> add(@RequestBody CartItem item) {
        return ResponseEntity.ok(cartItemService.addItemToCart(item));
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> list(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getItemsForCart(cartId));
    }
}
