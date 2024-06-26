package com.PosSystem.POS.System.Controllers;

import com.PosSystem.POS.System.Dto.DrinkResponse;
import com.PosSystem.POS.System.Dto.DrinkDTO;
import com.PosSystem.POS.System.Service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/drinks")
public class DrinkControllers {

    @Autowired
    DrinkService drinkService;

    @PostMapping("/create")
    DrinkResponse createDrink(@RequestBody DrinkDTO drinkDTO){
        return drinkService.createDrink(drinkDTO);
    }

    @GetMapping("/getAllDrinks")
    DrinkResponse getAllDrinks(){
        return drinkService.getAllDrinks();
    }

    @GetMapping("/getDrinksByCategory/{category}")
    DrinkResponse getDrinksByCategory(@PathVariable String category){
        try{
            Long categoryID = Long.parseLong(category);

            return drinkService.getDrinksByCategory(Optional.of(categoryID), Optional.empty());
        }catch (NumberFormatException e){
            return drinkService.getDrinksByCategory(Optional.empty(), Optional.of(category));
        }
    }
    @GetMapping("/getDrink/{drink}")
    DrinkResponse getDrinkInfo(@PathVariable String drink){
        try{
            Long drinkId = Long.parseLong(drink);

            return drinkService.getDrinkInfo(Optional.of(drinkId), Optional.empty());
        }catch (NumberFormatException e){
            return drinkService.getDrinkInfo(Optional.empty(), Optional.of(drink));
        }
    }

    @PutMapping("/update/{drink}")
    DrinkResponse updateDrink(@PathVariable String drink, @RequestBody DrinkDTO drinkDTO){
        try{
            Long drinkID = Long.parseLong(drink);

            return drinkService.updateDrink(Optional.of(drinkID), Optional.empty(), drinkDTO);
        }catch (NumberFormatException e){
            return drinkService.updateDrink(Optional.empty(), Optional.of(drink), drinkDTO);
        }
    }


    @DeleteMapping("/delete/{drink}")
    DrinkResponse deleteDrink(@PathVariable String drink){
        try{
            Long drinkID = Long.parseLong(drink);

            return drinkService.deleteDrink(Optional.of(drinkID), Optional.empty());
        }catch (NumberFormatException e){
            return drinkService.deleteDrink(Optional.empty(), Optional.of(drink));
        }
    }
}
