package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.CategoriesRepo;
import com.PosSystem.POS.System.Dto.CategoryDetails;
import com.PosSystem.POS.System.Dto.CategoryResponse;
import com.PosSystem.POS.System.Dto.CategoryDTO;
import com.PosSystem.POS.System.Entity.Categories;
import com.PosSystem.POS.System.Utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoriesRepo categoriesRepo;
    @Override
    public CategoryResponse createCategory(CategoryDTO categoryDTO) {
        if (categoryExist(categoryDTO.getCategoryName())){
            return CategoryResponse.builder()
                    .responseCode(Messages.FAIL_CODE)
                    .responseMessage(Messages.FAIL_MESSAGE)
                    .build();
        }

        Categories newCategory = Categories.builder()
                .categoryName(categoryDTO.getCategoryName())
                .build();

        categoriesRepo.save(newCategory);
        return CategoryResponse.builder()
                .responseCode(Messages.CREATED_CODE)
                .responseMessage(Messages.CREATED_MESSAGE)
                .categoryName(categoryDTO.getCategoryName())
                .build();
    }

    @Override
    public CategoryResponse updateCategory(Optional<Long> categoryID, Optional<String> categoryName, CategoryDTO categoryDTO) {
        Optional<Categories> category = findCategory(categoryID, categoryName);

        if(category.isEmpty()){
            return CategoryResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        if(categoryExist(category.get().getCategoryName()) && !categoryExist(categoryDTO.getCategoryName())){
            Categories existingCategory = category.get();
            existingCategory.setCategoryName(categoryDTO.getCategoryName());
            categoriesRepo.save(existingCategory);

            return CategoryResponse.builder()
                    .responseCode(Messages.UPDATED_CODE)
                    .responseMessage(Messages.UPDATED_MESSAGE)
                    .categoryName(categoryDTO.getCategoryName())
                    .build();
        }
        return CategoryResponse.builder()
                .responseCode(Messages.ALREADY_EXISTS_CODE)
                .responseMessage(Messages.ALREADY_EXISTS_MESSAGE)
                .build();
    }
    @Override
    public CategoryResponse getAllCategories() {
        List<Categories> categories = categoriesRepo.findAll();

        List<CategoryDetails> categoryDetails = categories.stream()
                .map(category -> CategoryDetails.builder()
                        .categoryName(category.getCategoryName())
                        .build())
                .collect(Collectors.toList());

        if(!categories.isEmpty()) {
            return CategoryResponse.builder()
                    .responseCode(Messages.SUCCESS_CODE)
                    .responseMessage(Messages.SUCCESS_MESSAGE)
                    .categoryDetails(categoryDetails)
                    .build();
        }  return CategoryResponse.builder()
                .responseCode(Messages.FAIL_CODE)
                .responseMessage(Messages.FAIL_MESSAGE)
                .categoryDetails(null)
                .build();

    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Optional<Categories> category = categoriesRepo.findById(id);
        if (categoryExist(category.get().getCategoryName())) {
            return CategoryResponse.builder()
                    .categoryName(category.get().getCategoryName())
                    .responseCode(Messages.SUCCESS_CODE)
                    .responseMessage(Messages.SUCCESS_MESSAGE)
                    .build();
        }
        return CategoryResponse.builder()
                .responseCode(Messages.FAIL_CODE)
                .responseMessage(Messages.FAIL_MESSAGE)
                .build();
    }

    @Override
    public CategoryResponse deleteCategory(Optional<Long> categoryId, Optional<String> categoryName) {

        Optional<Categories> categoryToDelete = findCategory(categoryId, categoryName);

        if(categoryToDelete.isEmpty()){
            return CategoryResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        categoriesRepo.delete(categoryToDelete.get());

        return CategoryResponse.builder()
                .responseCode(Messages.DELETED_CODE)
                .responseMessage(Messages.DELETED_MESSAGE)
                .build();
    }

    private Optional<Categories> findCategory(Optional<Long> categoryID, Optional<String> categoryName){
        Optional<Categories> findCategories = Optional.empty();

        if (categoryID.isPresent() && categoriesRepo.existsById(categoryID.get())) {
            findCategories = categoriesRepo.findById(categoryID.get());
        } else if(categoryName.isPresent() && categoriesRepo.existsByCategoryName(categoryName.get())) {
            findCategories = categoriesRepo.findByCategoryName(categoryName.get());
        }
        return findCategories;
    }

    private boolean categoryExist(String name){
        return categoriesRepo.existsByCategoryName(name);
    }
}
