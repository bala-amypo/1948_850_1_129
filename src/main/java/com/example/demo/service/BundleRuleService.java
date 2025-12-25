package com.example.demo.service;

import java.util.List;
import com.example.demo.model.BundleRule;

public interface BundleRuleService {

    BundleRule createRule(BundleRule rule);

    List<BundleRule> getActiveRules();
}
