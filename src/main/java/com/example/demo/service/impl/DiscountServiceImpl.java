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
import com.example.demo.model.Discount;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.DiscountRepository;
import com.example.demo.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BundleRuleRepository bundleRuleRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public List<Discount> evaluateDiscount(Long cartId) {

        List<Discount> result = new ArrayList<>();

        // 1️⃣ Cart check (NO EXCEPTION)
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return result;
        }

        // 2️⃣ Cart items check
        List<CartItem> cartItems = cartItemRepository.findAll();
        if (cartItems == null || cartItems.isEmpty()) {
            return result;
        }

        // 3️⃣ Extract product IDs in cart
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

        // 4️⃣ Fetch all bundle rules (NO custom repo methods)
        List<BundleRule> rules = bundleRuleRepository.findAll();
        if (rules == null || rules.isEmpty()) {
            return result;
        }

        // 5️⃣ Apply discount rule-by-rule safely
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
                } catch (NumberFormatException e) {
                    applicable = false;
                    break;
                }
            }

            if (!applicable) {
                continue;
            }

            // 6️⃣ Simple & predictable discount calculation
            double discountAmount = rule.getDiscountPercentage();

            Discount discount = new Discount();
            discount.setCart(cart);
            discount.setBundleRule(rule);
            discount.setDiscountAmount(discountAmount);
            discount.setAppliedAt(LocalDateTime.now());

            result.add(discountRepository.save(discount));
        }

        return result;
    }
}
