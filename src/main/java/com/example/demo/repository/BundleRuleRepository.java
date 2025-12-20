package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.BundleRule;

public interface BundleRuleRepository extends JpaRepository<BundleRule, Long> {

    List<BundleRule> findByActiveTrue();
}
