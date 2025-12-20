package com.example.demo.service;

import java.util.List;
import com.example.demo.model.BundleRule; // make sure you have this model class

public interface BundleRuleService {
    BundleRule createBundleRule(BundleRule bundleRule);
    BundleRule getBundleRuleById(Long id);
    List<BundleRule> getAllBundleRules();
    BundleRule updateBundleRule(Long id, BundleRule bundleRule);
    void deleteBundleRule(Long id);
}
