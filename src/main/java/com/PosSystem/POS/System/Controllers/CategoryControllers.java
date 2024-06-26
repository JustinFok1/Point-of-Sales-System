package com.PosSystem.POS.System.Controllers;

import com.PosSystem.POS.System.Dao.CategoriesRepo;
import com.PosSystem.POS.System.Dto.CategoryResponse;
import com.PosSystem.POS.System.Dto.CategoryDTO;
import com.PosSystem.POS.System.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryControllers {
    @Autowired
    CategoriesRepo categoriesRepo;
    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    CategoryResponse createCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.createCategory(categoryDTO);
    }
    @PutMapping("/update/{category}")
    CategoryResponse updateCategory(@PathVariable String category, @RequestBody CategoryDTO categoryDTO){
        try{
            Long categoryID = Long.parseLong(category);

            return categoryService.updateCategory(Optional.of(categoryID), Optional.empty(), categoryDTO);
        }catch (NumberFormatException e){
            return categoryService.updateCategory(Optional.empty(), Optional.of(category), categoryDTO);
        }
    }
    @GetMapping("/getAll")
    CategoryResponse getAllCategories(){
        return categoryService.getAllCategories();
    }
    @GetMapping("/get/{id}")
    CategoryResponse getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @DeleteMapping("/delete/{category}")
    CategoryResponse deleteCategory(@PathVariable String category) {
        try{
            Long categoryID = Long.parseLong(category);

            return categoryService.deleteCategory(Optional.of(categoryID), Optional.empty());
        }catch (NumberFormatException e){
            return categoryService.deleteCategory(Optional.empty(), Optional.of(category));
        }
    }
}
