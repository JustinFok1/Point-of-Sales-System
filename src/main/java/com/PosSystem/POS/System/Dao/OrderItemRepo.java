package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.Order;
import com.PosSystem.POS.System.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderNumberAndOrderOrderDate (int orderNumber, LocalDate orderDate);
}
