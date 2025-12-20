package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.BundleRule;
import com.example.demo.repository.BundleRuleRepository;

@Service
public class BundleRuleServiceImpl implements BundleRuleService {

    private final BundleRuleRepository bundleRuleRepository;

    public BundleRuleServiceImpl(BundleRuleRepository bundleRuleRepository) {
        this.bundleRuleRepository = bundleRuleRepository;
    }

    @Override
    public BundleRule createBundleRule(BundleRule bundleRule) {
        return bundleRuleRepository.save(bundleRule);
    }

    @Override
    public BundleRule getBundleRuleById(Long id) {
        return bundleRuleRepository.findById(id).orElse(null);
    }

    @Override
    public List<BundleRule> getAllBundleRules() {
        return bundleRuleRepository.findAll();
    }

    @Override
    public BundleRule updateBundleRule(Long id, BundleRule bundleRule) {
        Optional<BundleRule> existing = bundleRuleRepository.findById(id);
        if (existing.isPresent()) {
            BundleRule br = existing.get();
            br.setName(bundleRule.getName()); // example field
            br.setDescription(bundleRule.getDescription()); // example field
            return bundleRuleRepository.save(br);
        }
        return null;
    }

    @Override
    public void deleteBundleRule(Long id) {
        bundleRuleRepository.deleteById(id);
    }
}
