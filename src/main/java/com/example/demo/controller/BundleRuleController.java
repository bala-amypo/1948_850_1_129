package com.example.demo.controller;

import com.example.demo.model.BundleRule;
import com.example.demo.service.BundleRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/bundle-rules")
@SecurityRequirement(name="bearerAuth")
public class BundleRuleController {

    private final BundleRuleService service;

    public BundleRuleController(BundleRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BundleRule> create(@RequestBody BundleRule rule) {
        return ResponseEntity.ok(service.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody BundleRule rule) {
        service.updateRule(id, rule);
        return ResponseEntity.ok("Bundle rule updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BundleRule> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRuleById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BundleRule>> active() {
        return ResponseEntity.ok(service.getActiveRules());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        service.deactivateRule(id);
        return ResponseEntity.ok("Bundle rule deactivated successfully");
    }
}
