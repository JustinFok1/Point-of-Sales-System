package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.Toppings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ToppingsRepo extends JpaRepository<Toppings, Long> {
    Boolean existsByToppingName(String toppingName);
    boolean existsById(Long toppingId);
    Optional<Toppings> findById(Long toppingId);
    Optional<Toppings> findByToppingName(String name);
}
