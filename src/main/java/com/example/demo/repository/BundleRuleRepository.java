package com.example.demo.repository;

import com.example.demo.model.BundleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BundleRuleRepository extends JpaRepository<BundleRule, Long> {
    // ❌ DO NOT add any custom method
}
