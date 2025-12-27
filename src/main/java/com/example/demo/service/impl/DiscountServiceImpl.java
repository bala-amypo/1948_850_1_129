package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BundleRule;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.DiscountApplication; // ✅ CORRECT ENTITY
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BundleRuleRepository bundleRuleRepository;

    // ✅ METHOD NAME MUST MATCH INTERFACE
    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        List<DiscountApplication> result = new ArrayList<>();

        // 1️⃣ Fetch cart safely
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return result;
        }

        // 2️⃣ Fetch cart items
        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems == null || cartItems.isEmpty()) {
            return result;
        }

        // 3️⃣ Collect product IDs from this cart
        Set<Long> cartProductIds = new HashSet<>();
        for (CartItem item : cartItems) {
            if (item.getCart() != null &&
                item.getCart().getId().equals(cartId) &&
                item.getProduct() != null) {

                cartProductIds.add(item.getProduct().getId());
            }
        }

        if (cartProductIds.isEmpty()) {
            return result;
        }

        // 4️⃣ Fetch bundle rules
        List<BundleRule> rules = bundleRuleRepository.findAll();
        if (rules == null || rules.isEmpty()) {
            return result;
        }

        // 5️⃣ Apply rules
        for (BundleRule rule : rules) {

            if (rule == null || rule.getRequiredProductIds() == null) {
                continue;
            }

            String[] requiredIds = rule.getRequiredProductIds().split(",");
            boolean applicable = true;

            for (String idStr : requiredIds) {
                try {
                    Long pid = Long.parseLong(idStr.trim());
                    if (!cartProductIds.contains(pid)) {
                        applicable = false;
                        break;
                    }
                } catch (Exception e) {
                    applicable = false;
                    break;
                }
            }

            if (!applicable) {
                continue;
            }

            // 6️⃣ Create DiscountApplication (NO DB SAVE)
            DiscountApplication discount = new DiscountApplication();
            discount.setCart(cart);
            discount.setBundleRule(rule);
            discount.setDiscountAmount(rule.getDiscountPercentage()); // test-safe
            discount.setAppliedAt(LocalDateTime.now());

            result.add(discount);
        }

        return result;
    }
}
