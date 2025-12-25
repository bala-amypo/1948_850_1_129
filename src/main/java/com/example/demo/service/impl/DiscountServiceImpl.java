package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.model.DiscountApplication;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.BundleRule;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.DiscountApplicationRepository;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BundleRuleRepository bundleRuleRepository;
    private final DiscountApplicationRepository discountApplicationRepository;

    public DiscountServiceImpl(CartRepository cartRepository,
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
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive()) {
            return Collections.emptyList();
        }

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();

        List<DiscountApplication> appliedDiscounts = new ArrayList<>();

        for (BundleRule rule : rules) {
            List<Long> requiredIds = Arrays.stream(rule.getRequiredProductIds().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).map(Long::parseLong).toList();

            boolean allPresent = requiredIds.stream()
                    .allMatch(id -> items.stream().anyMatch(ci -> ci.getProduct().getId().equals(id)));

            if (allPresent) {
                BigDecimal total = items.stream()
                        .filter(ci -> requiredIds.contains(ci.getProduct().getId()))
                        .map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal discountAmount = total.multiply(BigDecimal.valueOf(rule.getDiscountPercentage())).divide(BigDecimal.valueOf(100));

                // Clear previous discounts for this cart
                discountApplicationRepository.deleteByCartId(cartId);

                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discountAmount);
                app.setAppliedAt(LocalDateTime.now());

                appliedDiscounts.add(discountApplicationRepository.save(app));
            }
        }

        return appliedDiscounts;
    }
}
