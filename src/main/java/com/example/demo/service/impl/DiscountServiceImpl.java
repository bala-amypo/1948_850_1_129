package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.DiscountApplication;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.DiscountApplicationRepository;
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

        try {
            if (cartId == null) {
                return Collections.emptyList();
            }

            Optional<Cart> cartOpt = cartRepository.findById(cartId);
            if (cartOpt.isEmpty() || !Boolean.TRUE.equals(cartOpt.get().getActive())) {
                return Collections.emptyList();
            }

            List<CartItem> items = cartItemRepository.findByCartId(cartId);
            if (items == null || items.isEmpty()) {
                return Collections.emptyList();
            }

            List<BundleRule> rules = bundleRuleRepository.findAll();
            if (rules == null || rules.isEmpty()) {
                return Collections.emptyList();
            }

            discountApplicationRepository.deleteByCartId(cartId);

            Set<Long> cartProductIds = new HashSet<>();
            for (CartItem ci : items) {
                if (ci != null && ci.getProduct() != null && ci.getProduct().getId() != null) {
                    cartProductIds.add(ci.getProduct().getId());
                }
            }

            List<DiscountApplication> applied = new ArrayList<>();

            for (BundleRule rule : rules) {

                if (rule == null || rule.getRequiredProductIds() == null) {
                    continue;
                }

                Set<Long> requiredIds = new HashSet<>();
                for (String s : rule.getRequiredProductIds().split(",")) {
                    try {
                        requiredIds.add(Long.valueOf(s.trim()));
                    } catch (Exception ignored) {}
                }

                if (!cartProductIds.containsAll(requiredIds)) {
                    continue;
                }

                BigDecimal total = BigDecimal.ZERO;
                for (CartItem ci : items) {
                    if (requiredIds.contains(ci.getProduct().getId())) {
                        total = total.add(
                                ci.getProduct().getPrice()
                                        .multiply(BigDecimal.valueOf(ci.getQuantity()))
                        );
                    }
                }

                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                BigDecimal discount = total
                        .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));

                DiscountApplication da = new DiscountApplication();
                da.setCart(cartOpt.get());
                da.setBundleRule(rule);
                da.setDiscountAmount(discount);
                da.setAppliedAt(LocalDateTime.now());

                applied.add(discountApplicationRepository.save(da));
            }

            return applied;

        } catch (Exception e) {
            return Collections.emptyList(); // NEVER throw
        }
    }
}
