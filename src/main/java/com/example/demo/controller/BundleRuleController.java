package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.impl.BundleRuleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bundle-rules")
@Tag(name = "Bundle Rules", description = "APIs for bundle discount rules")
public class BundleRuleController {
    private final BundleRuleServiceImpl bundleRuleService;
    
    public BundleRuleController(BundleRuleServiceImpl bundleRuleService) {
        this.bundleRuleService = bundleRuleService;
    }
    
    @PostMapping
    @Operation(summary = "Create bundle rule", description = "Creates a new bundle discount rule")
    public ResponseEntity<BundleRule> createRule(@RequestBody BundleRule rule) {
        BundleRule created = bundleRuleService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}