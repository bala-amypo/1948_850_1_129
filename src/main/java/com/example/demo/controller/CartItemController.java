package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import java.util.List;

@RestController
@RequestMapping("/cart-items")
@SecurityRequirement(name="bearerAuth")

public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestParam Long cartId,
                                            @RequestParam Long productId,
                                            @RequestParam Integer quantity) {

        CartItem item = new CartItem();

        Cart cart = new Cart();
        cart.setId(cartId);

        Product product = new Product();
        product.setId(productId);

        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        CartItem savedItem = cartItemService.addItemToCart(item);
        return ResponseEntity.ok(savedItem);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getItemsForCart(cartId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id,
                                             @RequestParam Integer quantity) {
        cartItemService.updateItem(id, quantity);
        return ResponseEntity.ok("Cart item updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeItem(@PathVariable Long id) {
        cartItemService.removeItem(id);
        return ResponseEntity.ok("Cart item removed successfully");
    }
}
