package com.example.CGC.Database;

import com.example.CGC.Schemas.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProductDatabase extends JpaRepository<Product, UUID> {
    List<Product> findByproductNameLike(String productName);
    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET quantity = :quantity WHERE id = :id", nativeQuery = true)
    void updateQuantity(@Param("id") UUID id, @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM products WHERE id = :productId", nativeQuery = true)
    Product getProductByID(UUID productId);

    @Query(value = "SELECT quantity FROM products WHERE id = :productId", nativeQuery = true)
    int getProductQuantityByID(UUID productId);
}
