package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DiscountServiceImpl {

    private final DiscountApplicationRepository discountApplicationRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public DiscountServiceImpl(DiscountApplicationRepository discountApplicationRepository,
                               BundleRuleRepository bundleRuleRepository,
                               CartRepository cartRepository,
                               CartItemRepository cartItemRepository) {
        this.discountApplicationRepository = discountApplicationRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null || !cart.getActive()) {
            return Collections.emptyList();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        discountApplicationRepository.deleteByCartId(cartId);

        Set<Long> productIdsInCart = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            productIdsInCart.add(ci.getProduct().getId());
            total = total.add(ci.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findByActiveTrue()) {

            Set<Long> required = new HashSet<>();
            for (String id : rule.getRequiredProductIds().split(",")) {
                required.add(Long.valueOf(id.trim()));
            }

            if (productIdsInCart.containsAll(required)) {
                BigDecimal discountAmount = total
                        .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discountAmount);
                app.setAppliedAt(LocalDateTime.now());

                applied.add(discountApplicationRepository.save(app));
            }
        }

        return applied;
    }
}
