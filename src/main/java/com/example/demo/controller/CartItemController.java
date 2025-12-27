package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.impl.CartItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart-items")
@Tag(name = "Cart Items", description = "APIs for cart items")
public class CartItemController {
    private final CartItemServiceImpl cartItemService;
    
    public CartItemController(CartItemServiceImpl cartItemService) {
        this.cartItemService = cartItemService;
    }
    
    @PostMapping
    @Operation(summary = "Add item to cart", description = "Adds product to cart or updates quantity")
    public ResponseEntity<CartItem> addItem(@RequestBody Map<String, Object> request) {
        CartItem item = new CartItem();
        
        Cart cart = new Cart();
        cart.setId(Long.valueOf(request.get("cartId").toString()));
        item.setCart(cart);
        
        Product product = new Product();
        product.setId(Long.valueOf(request.get("productId").toString()));
        item.setProduct(product);
        
        item.setQuantity(Integer.valueOf(request.get("quantity").toString()));
        
        CartItem added = cartItemService.addItemToCart(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }
    
    @GetMapping("/cart/{cartId}")
    @Operation(summary = "Get cart items", description = "Gets all items in the cart")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        List<CartItem> items = cartItemService.getItemsForCart(cartId);
        return ResponseEntity.ok(items);
    }
}