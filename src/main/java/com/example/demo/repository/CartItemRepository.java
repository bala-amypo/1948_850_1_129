package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);

    // Used in HQL / HCQL test
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.quantity >= :minQty")
    List<CartItem> findByCartIdAndMinQuantity(
            @Param("cartId") Long cartId,
            @Param("minQty") Integer minQty
    );
}
