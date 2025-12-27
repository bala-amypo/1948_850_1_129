package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {
    private final BundleRuleRepository bundleRuleRepository;
    
    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }
    
    @Override
    @Transactional
    public BundleRule createRule(BundleRule rule) {
        if (rule.getDiscountPercentage() < 0 || rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        if (rule.getRequiredProductIds() == null || 
            rule.getRequiredProductIds().trim().isEmpty()) {
            throw new IllegalArgumentException("Required product IDs cannot be empty");
        }
        return bundleRuleRepository.save(rule);
    }
}
