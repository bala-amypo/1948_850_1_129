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

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) return Collections.emptyList();

        Set<Long> cartProductIds = new HashSet<>();
        for (CartItem item : cartItems) {
            if (item.getProduct() != null) {
                cartProductIds.add(item.getProduct().getId());
            }
        }

        List<BundleRule> rules = bundleRuleRepository.findAll();
        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : rules) {

            if (rule.getActive() == null || !rule.getActive()) continue;
            if (rule.getRequiredProductIds() == null) continue;

            Set<Long> requiredIds = new HashSet<>();
            for (String id : rule.getRequiredProductIds().split(",")) {
                requiredIds.add(Long.parseLong(id.trim()));
            }

            if (!cartProductIds.containsAll(requiredIds)) continue;

            BigDecimal total = BigDecimal.ZERO;

            for (CartItem ci : cartItems) {
                if (ci.getProduct() == null || ci.getProduct().getPrice() == null) continue;

                if (requiredIds.contains(ci.getProduct().getId())) {
                    total = total.add(
                            ci.getProduct().getPrice()
                                    .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    );
                }
            }

            if (total.compareTo(BigDecimal.ZERO) == 0) continue;

            BigDecimal discount = total
                    .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                    .divide(BigDecimal.valueOf(100));

            DiscountApplication app = new DiscountApplication();
            app.setCart(cart);
            app.setBundleRule(rule);
            app.setDiscountAmount(discount);
            app.setAppliedAt(LocalDateTime.now());

            result.add(discountApplicationRepository.save(app));
        }

        return result;
    }
}
