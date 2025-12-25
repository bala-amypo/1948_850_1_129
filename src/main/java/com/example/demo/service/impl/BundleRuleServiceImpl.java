package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;

@Service
public class BundleRuleServiceImpl {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    public BundleRule createRule(BundleRule rule) {

        if (rule.getDiscountPercentage() < 0 ||
            rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("between 0 and 100");
        }

        if (rule.getRequiredProductIds() == null ||
            rule.getRequiredProductIds().trim().isEmpty()) {
            throw new IllegalArgumentException("cannot be empty");
        }

        return bundleRuleRepository.save(rule);
    }
}
