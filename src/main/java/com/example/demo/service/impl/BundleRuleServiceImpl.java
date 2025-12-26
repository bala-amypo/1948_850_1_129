package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import org.springframework.stereotype.Service;

@Service
public class BundleRuleServiceImpl {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    public BundleRule createRule(BundleRule rule) {

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().trim().isEmpty()) {
            throw new IllegalArgumentException("Required products cannot be empty");
        }

        if (rule.getDiscountPercentage() == null ||
                rule.getDiscountPercentage() < 0 ||
                rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        return bundleRuleRepository.save(rule);
    }
}
