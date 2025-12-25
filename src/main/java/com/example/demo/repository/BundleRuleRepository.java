package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.BundleRule;

public interface BundleRuleRepository {
    BundleRule save(BundleRule rule);
    List<BundleRule> findByActiveTrue();
}
