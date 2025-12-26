package com.example.demo.repository;

import com.example.demo.model.DiscountApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountApplicationRepository extends JpaRepository<DiscountApplication, Long> {

    void deleteByCartId(Long cartId);

    List<DiscountApplication> findByCartId(Long cartId);
}
