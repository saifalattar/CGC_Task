package com.example.CGC.Database;

import com.example.CGC.Schemas.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartsDatabase extends JpaRepository<Carts, String> { }
