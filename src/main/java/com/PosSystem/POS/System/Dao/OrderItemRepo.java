package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
}
