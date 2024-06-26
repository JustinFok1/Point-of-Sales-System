package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.CategoriesRepo;
import com.PosSystem.POS.System.Dao.DrinkRepos;
import com.PosSystem.POS.System.Dto.DrinkDetails;
import com.PosSystem.POS.System.Dto.DrinkResponse;
import com.PosSystem.POS.System.Dto.DrinkDTO;
import com.PosSystem.POS.System.Entity.Categories;
import com.PosSystem.POS.System.Entity.Drinks;
import com.PosSystem.POS.System.Utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrinkServiceImpl implements DrinkService {
    @Autowired
    DrinkRepos drinkRepos;
    @Autowired
    CategoriesRepo categoriesRepo;

    @Override
    public DrinkResponse createDrink(DrinkDTO drinkDTO) {
        Optional<Categories> category = categoriesRepo.findByCategoryName(drinkDTO.getCategoryName());
        if (drinkRepos.existsByDrinkNameAndCategoryCategoryName(drinkDTO.getDrinkName(), drinkDTO.getCategoryName())) {
            return DrinkResponse.builder()
                    .responseCode(Messages.ALREADY_EXISTS_CODE)
                    .responseMessage(Messages.ALREADY_EXISTS_MESSAGE)
                    .build();
        }
        if(!(categoriesRepo.existsByCategoryName(drinkDTO.getCategoryName()))){
            return DrinkResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage("Category: " + Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        Drinks drink = Drinks.builder()
                .drinkName(drinkDTO.getDrinkName())
                .price(drinkDTO.getPrice())
                .category(category.get())
                .categoryName(category.get().getCategoryName())
                .build();

        drinkRepos.save(drink);

        return DrinkResponse.builder()
                .responseCode(Messages.CREATED_CODE)
                .responseMessage(Messages.CREATED_MESSAGE)
                .drinkName(drinkDTO.getDrinkName())
                .price(drinkDTO.getPrice())
                .build();
    }

    @Override
    public DrinkResponse updateDrink(Optional<Long> drinkId, Optional<String> drinkName, DrinkDTO drinkDTO) {
        Optional<Drinks> drinkToUpdate = findDrink(drinkId, drinkName);

        if(drinkToUpdate.isEmpty()){
            return DrinkResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .drinks(null)
                    .build();
        }

        if (!drinkRepos.existsByDrinkName(drinkDTO.getDrinkName())) {
            Drinks existingDrink = drinkToUpdate.get();
            existingDrink.setDrinkName(drinkDTO.getDrinkName());
            existingDrink.setPrice(drinkDTO.getPrice());

            drinkRepos.save(existingDrink);

            return DrinkResponse.builder()
                    .responseCode(Messages.UPDATED_CODE)
                    .responseMessage(Messages.UPDATED_MESSAGE)
                    .drinkName(existingDrink.getDrinkName())
                    .price(existingDrink.getPrice())
                    .build();
        }
        return DrinkResponse.builder()
                .responseCode(Messages.ALREADY_EXISTS_CODE)
                .responseMessage(Messages.ALREADY_EXISTS_MESSAGE)
                .build();
    }


    @Override
    public DrinkResponse deleteDrink(Optional<Long> drinkID, Optional<String> drinkName) {
        Optional<Drinks> drinkToDelete = findDrink(drinkID, drinkName);

       if(drinkToDelete.isEmpty()){
            return DrinkResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .drinks(null)
                    .build();
        }

        drinkRepos.delete(drinkToDelete.get());
        return DrinkResponse.builder()
                .responseCode(Messages.DELETED_CODE)
                .responseMessage(Messages.DELETED_MESSAGE)
                .build();

    }

    @Override
    public DrinkResponse getAllDrinks() {
        List<Drinks> drinks = drinkRepos.findAll();

        List<DrinkDetails> drinkDetails = drinks.stream()
                .map(drink -> DrinkDetails.builder()
                        .drinkName(drink.getDrinkName())
                        .price(drink.getPrice())
                        .categoryName(drink.getCategory().getCategoryName())
                        .build())
                .collect(Collectors.toList());

        if (drinks.isEmpty() && categoriesRepo.findAll().isEmpty()) {
            return DrinkResponse.builder()
                    .responseCode(Messages.EMPTY_CODE)
                    .responseMessage(Messages.EMPTY_MESSAGE)
                    .drinks(null)
                    .build();
        }

        return DrinkResponse.builder()
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .drinks(drinkDetails)
                .build();
    }

    @Override
    public DrinkResponse getDrinksByCategory(Optional<Long> categoryId, Optional<String> categoryName) {
        List<Drinks> drinks;

        if(categoryId.isPresent() && categoriesRepo.existsById(categoryId.get())){
            drinks = drinkRepos.findByCategoryId(categoryId.get());
        }else if(categoryName.isPresent() && categoriesRepo.existsByCategoryName(categoryName.get())){
            drinks = drinkRepos.findByCategoryCategoryName(categoryName.get());
        }else{
            return DrinkResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .drinks(null)
                    .build();
        }

        List<DrinkDetails> drinkDetails = drinks.stream()
                .map(drink -> DrinkDetails.builder()
                        .drinkName(drink.getDrinkName())
                        .price(drink.getPrice())
                        .categoryName(drink.getCategory().getCategoryName())
                        .build())
                .collect(Collectors.toList());

        if (!drinks.isEmpty()) {
            return DrinkResponse.builder()
                    .responseCode(Messages.SUCCESS_CODE)
                    .responseMessage(Messages.SUCCESS_MESSAGE)
                    .drinks(drinkDetails)
                    .build();
        }

        return DrinkResponse.builder()
                .responseCode(Messages.EMPTY_CODE)
                .responseMessage(Messages.EMPTY_MESSAGE)
                .drinks(null)
                .build();
    }

    public DrinkResponse getDrinkInfo(Optional<Long> drinkId, Optional<String> drinkName){
        Optional<Drinks> foundDrink = findDrink(drinkId, drinkName);
        if(foundDrink.isEmpty()){
            return DrinkResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        DrinkDetails drinkDetails = DrinkDetails.builder()
                .categoryName(foundDrink.get().getCategoryName())
                .drinkName(foundDrink.get().getDrinkName())
                .price(foundDrink.get().getPrice())
                .build();

        return DrinkResponse.builder()
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .drinkDetails(drinkDetails)
                .build();
    }

    private Optional<Drinks> findDrink(Optional<Long> drinkID, Optional<String> drinkName) {
        Optional<Drinks> findDrink = Optional.empty();

        if (drinkID.isPresent() && drinkRepos.existsById(drinkID.get())) {
            findDrink = drinkRepos.findById(drinkID.get());
        } else if(drinkName.isPresent() && drinkRepos.existsByDrinkName(drinkName.get())) {
            findDrink = drinkRepos.findByDrinkName(drinkName.get());
        }
        return findDrink;
    }
}
