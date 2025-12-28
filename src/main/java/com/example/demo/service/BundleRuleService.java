package com.example.demo.service;

import java.util.List;
import com.example.demo.model.BundleRule;

public interface BundleRuleService {

    BundleRule createRule(BundleRule rule);

    BundleRule updateRule(Long id, BundleRule rule);

    BundleRule getRuleById(Long id);

    List<BundleRule> getActiveRules();

    void deactivateRule(Long id);
}
