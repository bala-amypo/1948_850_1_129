package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class BundleRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;
    private String requiredProductIds;
    private int discountPercentage;
    private boolean active = true;

    public Long getId() { return id; }
    public String getRuleName() { return ruleName; }
    public String getRequiredProductIds() { return requiredProductIds; }
    public int getDiscountPercentage() { return discountPercentage; }

    // ⭐ REQUIRED BY TEST
    public boolean isActive() { 
        return active; 
    }

    public void setId(Long id) { this.id = id; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public void setRequiredProductIds(String requiredProductIds) { this.requiredProductIds = requiredProductIds; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }
    public void setActive(boolean active) { this.active = active; }
}
