package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;

@Service
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private final CartRepository cartRepository;
    private final DiscountRepository discountRepository;
    private final BundleRuleRepository bundleRuleRepository;

    public DiscountServiceImpl(CartRepository cartRepository,
                               DiscountRepository discountRepository,
                               BundleRuleRepository bundleRuleRepository) {
        this.cartRepository = cartRepository;
        this.discountRepository = discountRepository;
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public List<Discount> evaluateDiscounts(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (!cart.getActive()) {
            return List.of();
        }

        List<Discount> applied = new ArrayList<>();

        for (BundleRule rule : bundleRuleRepository.findAll()) {

            if (!rule.isActive()) continue;

            BigDecimal total = BigDecimal.ZERO;

            for (CartItem ci : cart.getItems()) {
                total = total.add(
                        ci.getProduct()
                          .getPrice()
                          .multiply(BigDecimal.valueOf(ci.getQuantity()))
                );
            }

            Discount d = new Discount();
            d.setCart(cart);
            d.setBundleRule(rule);
            d.setDiscountAmount(
                    total.multiply(
                            BigDecimal.valueOf(rule.getDiscountPercentage())
                                    .divide(BigDecimal.valueOf(100))
                    )
            );
            d.setAppliedAt(LocalDateTime.now());

            applied.add(discountRepository.save(d));
        }

        return applied;
    }
}
