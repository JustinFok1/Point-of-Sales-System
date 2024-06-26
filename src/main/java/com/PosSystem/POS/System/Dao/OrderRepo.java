package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository

public interface OrderRepo  extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long orderID);
    Optional<Order> findByOrderItemsId(Long orderItemId);
    List<Order> findByCustomerNameAndPhoneNumber(String customerName, String phoneNumber);
    Optional<Order> findByOrderNumber(Integer orderNumber);
    Boolean existsByOrderNumberAndOrderDate(Integer orderNumber, LocalDate orderDate);


    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersOrderedByDateDesc();
    default Optional<Order> findLatestOrder() {
        List<Order> orders = findAllOrdersOrderedByDateDesc();
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    @Query("SELECT MAX(o.orderNumber) FROM Order o")
    Integer findMaxOrderNumber();
}
