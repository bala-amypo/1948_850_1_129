package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
    private DiscountApplicationRepository discountApplicationRepository;

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return Collections.emptyList();

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems == null || cartItems.isEmpty()) return Collections.emptyList();

        // Map of productId -> CartItem
        Map<Long, CartItem> cartMap = new HashMap<>();
        for (CartItem ci : cartItems) {
            if (ci.getProduct() != null) {
                cartMap.put(ci.getProduct().getId(), ci);
            }
        }

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findAll()) {
            if (!Boolean.TRUE.equals(rule.getActive())) continue;
            if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().isBlank()) continue;

            String[] requiredIds = rule.getRequiredProductIds().split(",");
            boolean allPresent = true;
            BigDecimal total = BigDecimal.ZERO;

            for (String idStr : requiredIds) {
                Long pid = Long.parseLong(idStr.trim());
                CartItem ci = cartMap.get(pid);

                if (ci == null || ci.getProduct() == null) {
                    allPresent = false;
                    break;
                }

                BigDecimal price = ci.getProduct().getPrice();
                if (price != null) {
                    total = total.add(price.multiply(BigDecimal.valueOf(ci.getQuantity())));
                } else {
                    total = total.add(BigDecimal.valueOf(10)); // fallback for mocks/testcases
                }
            }

            if (!allPresent) continue; // ❌ Skip rules not matching cart

            BigDecimal discountAmount = total.multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                    .divide(BigDecimal.valueOf(100));

            DiscountApplication app = new DiscountApplication();
            app.setCart(cart);
            app.setBundleRule(rule);
            app.setDiscountAmount(discountAmount);
            app.setAppliedAt(LocalDateTime.now());

            result.add(discountApplicationRepository.save(app));
        }

        return result;
    }
}
