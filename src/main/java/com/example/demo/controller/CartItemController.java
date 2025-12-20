package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.service.CartItemService;
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
    public CartItem addItem(@RequestBody CartItem item) {
        return service.addItemToCart(item);
    }

    @GetMapping("/cart/{cartId}")
    public List<CartItem> getItems(@PathVariable Long cartId) {
        return service.getItemsForCart(cartId);
    }
}
