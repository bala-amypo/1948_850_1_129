package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.demo.model.*;
import com.example.demo.repository.*;

@Service
public class DiscountServiceImpl {

    private final DiscountApplicationRepository discountApplicationRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public DiscountServiceImpl(
            DiscountApplicationRepository discountApplicationRepository,
            BundleRuleRepository bundleRuleRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository) {

        this.discountApplicationRepository = discountApplicationRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow();

        if (!cart.getActive()) {
            return Collections.emptyList();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();

        discountApplicationRepository.deleteByCartId(cartId);

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : rules) {

            Set<String> requiredIds =
                    new HashSet<>(Arrays.asList(rule.getRequiredProductIds().split(",")));

            BigDecimal total = BigDecimal.ZERO;
            Set<String> found = new HashSet<>();

            for (CartItem ci : cartItems) {
                String pid = ci.getProduct().getId().toString();
                if (requiredIds.contains(pid)) {
                    total = total.add(ci.getProduct().getPrice());
                    found.add(pid);
                }
            }

            if (found.containsAll(requiredIds)) {

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(
                        total.multiply(
                                BigDecimal.valueOf(rule.getDiscountPercentage() / 100)
                        )
                );
                app.setAppliedAt(LocalDateTime.now());

                result.add(discountApplicationRepository.save(app));
            }
        }
        return result;
    }
}
