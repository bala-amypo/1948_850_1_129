package com.example.demo.model;

public class BundleRule {

    private Long id;
    private String ruleName;
    private String requiredProductIds;
    private Double discountPercentage;
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getRequiredProductIds() { return requiredProductIds; }
    public void setRequiredProductIds(String requiredProductIds) { this.requiredProductIds = requiredProductIds; }

    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
