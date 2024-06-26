package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dto.CategoryResponse;
import com.PosSystem.POS.System.Dto.CategoryDTO;

import java.util.Optional;

public interface CategoryService {
    CategoryResponse createCategory(CategoryDTO categoryDTO);
    CategoryResponse updateCategory(Optional<Long> categoryID, Optional<String> categoryName, CategoryDTO categoryDTO);
    CategoryResponse getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse deleteCategory(Optional<Long> categoryId, Optional<String> categoryName);
}
