package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    Optional<Categories> findById (Long id);
    Boolean existsByCategoryName (String categoryName);
    boolean existsById (Long categoryID);
    Optional<Categories> findByCategoryName (String name);

}
