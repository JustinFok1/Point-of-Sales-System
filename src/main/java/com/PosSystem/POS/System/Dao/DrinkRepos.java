package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.Categories;
import com.PosSystem.POS.System.Entity.Drinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Repository

public interface DrinkRepos extends JpaRepository<Drinks, Long> {
    Boolean existsByDrinkName(String name);
    Boolean existsByDrinkNameAndCategoryCategoryName(String drinkName, String categoryName);
    boolean existsById(Long drinkId);
    List<Drinks> findByCategoryId(Long categoryId);
    List<Drinks> findByCategoryCategoryName(String name);
    Optional<Drinks> findByDrinkName(String drinkName);
}
