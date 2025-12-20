package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class DiscountServiceImpl implements DiscountService {

    private final DiscountApplicationRepository discountRepo;
    private final BundleRuleRepository ruleRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    public DiscountServiceImpl(DiscountApplicationRepository d, BundleRuleRepository r,
                               CartRepository c, CartItemRepository i) {
        this.discountRepo = d;
        this.ruleRepo = r;
        this.cartRepo = c;
        this.itemRepo = i;
    }

    @Override
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!cart.getActive()) return List.of();

        discountRepo.deleteByCartId(cartId);

        List<CartItem> items = itemRepo.findByCartId(cartId);
        List<BundleRule> rules = ruleRepo.findByActiveTrue();
        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : rules) {
            Set<Long> required = new HashSet<>();
            for (String s : rule.getRequiredProductIds().split(",")) {
                required.add(Long.parseLong(s.trim()));
            }

            Set<Long> present = new HashSet<>();
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem ci : items) {
                if (required.contains(ci.getProduct().getId())) {
                    present.add(ci.getProduct().getId());
                    total = total.add(
                        ci.getProduct().getPrice()
                          .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    );
                }
            }

            if (present.containsAll(required)) {
                DiscountApplication da = new DiscountApplication();
                da.setCart(cart);
                da.setBundleRule(rule);
                da.setDiscountAmount(
                        total.multiply(BigDecimal.valueOf(rule.getDiscountPercentage() / 100))
                );
                da.setAppliedAt(LocalDateTime.now());
                result.add(discountRepo.save(da));
            }
        }
        return result;
    }
}
