package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class DiscountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private BundleRule bundleRule;

    private BigDecimal discountAmount;
    private LocalDateTime appliedAt = LocalDateTime.now();

    public DiscountApplication() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public BundleRule getBundleRule() { return bundleRule; }
    public void setBundleRule(BundleRule bundleRule) {
        this.bundleRule = bundleRule;
    }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDateTime getAppliedAt() { return appliedAt; }
}
