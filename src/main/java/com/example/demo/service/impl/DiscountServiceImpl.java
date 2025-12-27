package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
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
    
    @Override
    @Transactional
    public List<DiscountApplication> evaluateDiscounts(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        
        if (!cart.getActive()) {
            return Collections.emptyList();
        }
        
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        Set<Long> cartProductIds = cartItems.stream()
                .map(item -> item.getProduct().getId())
                .collect(Collectors.toSet());
        
        List<BundleRule> activeRules = bundleRuleRepository.findByActiveTrue();
        discountApplicationRepository.deleteByCartId(cartId);
        
        List<DiscountApplication> applications = new ArrayList<>();
        
        for (BundleRule rule : activeRules) {
            String[] requiredIds = rule.getRequiredProductIds().split(",");
            Set<Long> requiredProductIds = Arrays.stream(requiredIds)
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            
            if (cartProductIds.containsAll(requiredProductIds)) {
                BigDecimal totalPrice = cartItems.stream()
                        .filter(item -> requiredProductIds.contains(item.getProduct().getId()))
                        .map(item -> item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                BigDecimal discountAmount = totalPrice
                        .multiply(BigDecimal.valueOf(rule.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));
                
                DiscountApplication app = new DiscountApplication();
                app.setCart(cart);
                app.setBundleRule(rule);
                app.setDiscountAmount(discountAmount);
                app.setAppliedAt(LocalDateTime.now());
                
                DiscountApplication saved = discountApplicationRepository.save(app);
                applications.add(saved);
            }
        }
        return applications;
    }
}