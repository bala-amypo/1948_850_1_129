package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        return ResponseEntity.ok(service.addItemToCart(item));
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(service.getItemsForCart(cartId));
    }
}
