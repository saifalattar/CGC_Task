package com.example.CGC.Database;

import com.example.CGC.Schemas.CGCOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.ArrayList;
import java.util.UUID;

public interface CGCOrderDatabase extends JpaRepository<CGCOrder, UUID> {
    @Query(value = "SELECT * FROM cgcorder WHERE user_id = :userId", nativeQuery = true)
    public ArrayList<CGCOrder> getAllUserOrders(UUID userId);
}
