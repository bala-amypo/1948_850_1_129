package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartId(Long cartId);

    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.quantity >= :minQty")
    List<CartItem> findByCartIdAndMinQuantity(@Param("cartId") Long cartId,
                                              @Param("minQty") Integer minQty);
}
