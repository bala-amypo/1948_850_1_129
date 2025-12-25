package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BundleRule;

@Repository
public interface BundleRuleRepository extends JpaRepository<BundleRule, Long> {

    List<BundleRule> findByActiveTrue();
}
