package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final DiscountApplicationRepository discountApplicationRepository;

    public DiscountServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            BundleRuleRepository bundleRuleRepository,
            DiscountApplicationRepository discountApplicationRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.discountApplicationRepository = discountApplicationRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return Collections.emptyList();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems == null || cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        List<BundleRule> rules = bundleRuleRepository.findAll();
        if (rules == null || rules.isEmpty()) {
            return Collections.emptyList();
        }

        discountApplicationRepository.deleteByCartId(cartId);

        Set<Long> cartProductIds = new HashSet<>();
        for (CartItem ci : cartItems) {
            if (ci.getProduct() != null && ci.getProduct().getId() != null) {
                cartProductIds.add(ci.getProduct().getId());
            }
        }

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : rules) {

            if (rule.getActive() == null || !rule.getActive()) continue;
            if (rule.getRequiredProductIds() == null ||
                rule.getRequiredProductIds().trim().isEmpty()) continue;

            Set<Long> requiredIds = new HashSet<>();
            for (String id : rule.getRequiredProductIds().split(",")) {
                requiredIds.add(Long.parseLong(id.trim()));
            }

            if (!cartProductIds.containsAll(requiredIds)) continue;

            BigDecimal total = BigDecimal.ZERO;

            for (CartItem ci : cartItems) {
                if (ci.getProduct() == null ||
                    ci.getProduct().getPrice() == null) continue;

                if (requiredIds.contains(ci.getProduct().getId())) {
                    total = total.add(
                            ci.getProduct().getPrice()
                                    .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    );
                }
            }

            if (total.compareTo(BigDecimal.ZERO) <= 0) continue;

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
