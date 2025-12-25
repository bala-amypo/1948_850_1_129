package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {

        // Validate discount percentage 0–100
        if (rule.getDiscountPercentage() == null ||
            rule.getDiscountPercentage() < 0 || 
            rule.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("between 0 and 100");
        }

        // Validate requiredProductIds not empty
        if (rule.getRequiredProductIds() == null ||
            rule.getRequiredProductIds().trim().isEmpty()) {
            throw new IllegalArgumentException("cannot be empty");
        }

        return bundleRuleRepository.save(rule);
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return bundleRuleRepository.findByActiveTrue();
    }
}
