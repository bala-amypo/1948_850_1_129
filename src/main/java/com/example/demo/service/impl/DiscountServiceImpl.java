// DiscountServiceImpl.java
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

    private final DiscountApplicationRepository discountRepo;
    private final BundleRuleRepository ruleRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    public DiscountServiceImpl(
            DiscountApplicationRepository d,
            BundleRuleRepository r,
            CartRepository c,
            CartItemRepository i) {
        this.discountRepo = d;
        this.ruleRepo = r;
        this.cartRepo = c;
        this.itemRepo = i;
    }

    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow();
        if (!cart.getActive()) return List.of();

        discountRepo.deleteByCartId(cartId);

        List<CartItem> items = itemRepo.findByCartId(cartId);
        Set<Long> productIds = items.stream()
                .map(i -> i.getProduct().getId())
                .collect(Collectors.toSet());

        List<DiscountApplication> result = new ArrayList<>();

        for (BundleRule rule : ruleRepo.findByActiveTrue()) {
            Set<Long> req = Arrays.stream(rule.getRequiredProductIds().split(","))
                    .map(Long::valueOf).collect(Collectors.toSet());

            if (productIds.containsAll(req)) {
                BigDecimal total = items.stream()
                        .filter(i -> req.contains(i.getProduct().getId()))
                        .map(i -> i.getProduct().getPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

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
