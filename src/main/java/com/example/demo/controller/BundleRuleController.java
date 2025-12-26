package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bundles")
public class BundleRuleController {

    private final BundleRuleService bundleRuleService;

    public BundleRuleController(BundleRuleService bundleRuleService) {
        this.bundleRuleService = bundleRuleService;
    }

    @PostMapping
    public ResponseEntity<BundleRule> createRule(@RequestBody BundleRule rule) {
        return ResponseEntity.ok(bundleRuleService.createRule(rule));
    }
}
