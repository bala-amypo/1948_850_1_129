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

    // ----------- ADD ITEM TO CART -----------

    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        CartItem saved = service.addItemToCart(item);
        return ResponseEntity.ok(saved);
    }

    // ----------- GET ITEMS FOR CART -----------

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable Long cartId) {
        List<CartItem> items = service.getItemsForCart(cartId);
        return ResponseEntity.ok(items);
    }
}
