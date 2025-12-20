package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.DiscountApplication;

public interface DiscountApplicationRepository
        extends JpaRepository<DiscountApplication, Long> {

    List<DiscountApplication> findByCartId(Long cartId);

    void deleteByCartId(Long cartId);
}
