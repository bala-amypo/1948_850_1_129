package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
