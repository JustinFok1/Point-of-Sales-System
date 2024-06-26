package com.PosSystem.POS.System.Service;


import com.PosSystem.POS.System.Dto.DrinkResponse;
import com.PosSystem.POS.System.Dto.DrinkDTO;

import java.util.Optional;

public interface DrinkService {
    DrinkResponse createDrink(DrinkDTO drinkDTO);
    DrinkResponse updateDrink(Optional<Long> drinkId, Optional<String> drinkName, DrinkDTO drinkDTO);
    DrinkResponse deleteDrink(Optional<Long> drinkId, Optional<String> drinkName);
    DrinkResponse getAllDrinks();
    DrinkResponse getDrinksByCategory(Optional<Long> categoryId, Optional<String> categoryName);
    DrinkResponse getDrinkInfo(Optional<Long> drinkId, Optional<String> drinkName);
}
