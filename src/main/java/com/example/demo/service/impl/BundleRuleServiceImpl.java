package com.example.demo.service.impl;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;
import com.example.demo.service.BundleRuleService;

import java.util.List;

public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository repo;

    public BundleRuleServiceImpl(BundleRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public BundleRule createRule(BundleRule rule) {
        if (rule.getDiscountPercentage() < 0 || rule.getDiscountPercentage() > 100)
            throw new IllegalArgumentException("between 0 and 100");

        if (rule.getRequiredProductIds() == null || rule.getRequiredProductIds().trim().isEmpty())
            throw new IllegalArgumentException("cannot be empty");

        return repo.save(rule);
    }

    @Override
    public List<BundleRule> getActiveRules() {
        return repo.findByActiveTrue();
    }
}
