package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.DiscountApplication;

@Repository
public interface DiscountApplicationRepository
        extends JpaRepository<DiscountApplication, Long> {

    void deleteByCartId(Long cartId);

    List<DiscountApplication> findByCartId(Long cartId);
}
