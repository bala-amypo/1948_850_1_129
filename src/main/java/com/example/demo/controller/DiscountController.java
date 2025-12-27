package com.example.demo.controller;

import com.example.demo.model.DiscountApplication;
import com.example.demo.service.impl.DiscountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@Tag(name = "Discounts", description = "APIs for discount evaluation")
public class DiscountController {
    private final DiscountServiceImpl discountService;
    
    public DiscountController(DiscountServiceImpl discountService) {
        this.discountService = discountService;
    }
    
    @PostMapping("/evaluate/{cartId}")
    @Operation(summary = "Evaluate discounts", description = "Evaluates and applies bundle discounts to cart")
    public ResponseEntity<List<DiscountApplication>> evaluateDiscounts(@PathVariable Long cartId) {
        List<DiscountApplication> applications = discountService.evaluateDiscounts(cartId);
        return ResponseEntity.ok(applications);
    }
}