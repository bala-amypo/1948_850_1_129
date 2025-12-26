// BundleRuleController.java
package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bundles")
public class BundleRuleController {

    private final BundleRuleService service;

    public BundleRuleController(BundleRuleService service) {
        this.service = service;
    }

    @PostMapping
    public BundleRule create(@RequestBody BundleRule rule) {
        return service.createRule(rule);
    }
}
