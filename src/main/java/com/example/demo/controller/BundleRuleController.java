package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bundles")
public class BundleRuleController {

    private final BundleRuleService service;

    public BundleRuleController(BundleRuleService service) {
        this.service = service;
    }

    @PostMapping
    public BundleRule create(@RequestBody BundleRule rule) {
        return service.createRule(rule);
    }

    @GetMapping("/active")
    public List<BundleRule> getActiveRules() {
        return service.getActiveRules();
    }
}
