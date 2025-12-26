package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;
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
            DiscountApplicationRepository discountApplicationRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bundleRuleRepository = bundleRuleRepository;
        this.discountApplicationRepository = discountApplicationRepository;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        // ✅ TESTCASE: inactive cart returns empty
        if (!cart.getActive()) {
            return Collections.emptyList();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        // clear previous discounts
        discountApplicationRepository.deleteByCartId(cartId);

        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();
        List<DiscountApplication> applied = new ArrayList<>();

        // productId → quantity map
        Set<Long> productIdsInCart = new HashSet<>();
        BigDecimal totalCartValue = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            productIdsInCart.add(ci.getProduct().getId());
            totalCartValue = totalCartValue.add(
                    ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()))
            );
        }

        for (BundleRule rule : rules) {

            if (!rule.getActive()) continue;

            String[] requiredIds = rule.getRequiredProductIds().split(",");
            boolean allMatch = true;

            for (String id : requiredIds) {
                Long pid = Long.parseLong(id.trim());
                if (!productIdsInCart.contains(pid)) {
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) {
                BigDecimal discount =
                        totalCartValue.multiply(
                                BigDecimal.valueOf(rule.getDiscountPercentage() / 100.0)
                        );

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discount);
                app.setAppliedAt(LocalDateTime.now());

                applied.add(discountApplicationRepository.save(app));
            }
        }

        return applied;
    }
}
