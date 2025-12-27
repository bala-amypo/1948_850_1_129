package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bundle_rules")
public class BundleRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String ruleName;
    
    @Column(nullable = false)
    private String requiredProductIds;
    
    @Column(nullable = false)
    private Double discountPercentage;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    public BundleRule() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRequiredProductIds() { return requiredProductIds; }
    public void setRequiredProductIds(String requiredProductIds) { 
        this.requiredProductIds = requiredProductIds; 
    }
    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { 
        this.discountPercentage = discountPercentage; 
    }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}