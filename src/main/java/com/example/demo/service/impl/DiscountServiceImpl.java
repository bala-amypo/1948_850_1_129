package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import org.springframework.stereotype.Service;

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

        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty() || !cartOpt.get().getActive()) {
            return Collections.emptyList(); // REQUIRED BY TESTS
        }

        Cart cart = cartOpt.get();

        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        List<BundleRule> rules = bundleRuleRepository.findByActiveTrue();
        if (rules == null || rules.isEmpty()) {
            return Collections.emptyList();
        }

        discountApplicationRepository.deleteByCartId(cartId);

        Set<Long> cartProductIds = items.stream()
                .map(ci -> ci.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> applied = new ArrayList<>();

        for (BundleRule rule : rules) {

            Set<Long> requiredIds = Arrays.stream(rule.getRequiredProductIds().split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            if (!cartProductIds.containsAll(requiredIds)) {
                continue;
            }

            BigDecimal total = items.stream()
                    .filter(ci -> requiredIds.contains(ci.getProduct().getId()))
                    .map(ci -> ci.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(ci.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal discount = total
                    .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                    .divide(BigDecimal.valueOf(100));

            DiscountApplication app = new DiscountApplication();
            app.setCart(cart);
            app.setBundleRule(rule);
            app.setDiscountAmount(discount);
            app.setAppliedAt(LocalDateTime.now());

            applied.add(discountApplicationRepository.save(app));
        }

        return applied;
    }
}
