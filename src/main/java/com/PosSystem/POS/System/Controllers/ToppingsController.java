package com.PosSystem.POS.System.Controllers;

import com.PosSystem.POS.System.Dao.ToppingsRepo;
import com.PosSystem.POS.System.Dto.ToppingsDTO;
import com.PosSystem.POS.System.Dto.ToppingsResponse;
import com.PosSystem.POS.System.Service.ToppingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/toppings")
public class ToppingsController {
    @Autowired
    ToppingsRepo toppingsRepo;
    @Autowired
    ToppingsService toppingsService;

    @PostMapping("/create")
    ToppingsResponse createTopping(@RequestBody ToppingsDTO toppingsDTO){
        return toppingsService.createTopping(toppingsDTO);
    }
    @PutMapping("/update/{topping}")
    ToppingsResponse updateTopping(@PathVariable String topping, @RequestBody ToppingsDTO toppingsDTO){
        try {
            Long toppingId = Long.parseLong(topping);

            return toppingsService.updateTopping(Optional.of(toppingId), Optional.empty(), toppingsDTO);
        }catch (NumberFormatException e){
            return toppingsService.updateTopping(Optional.empty(), Optional.of(topping), toppingsDTO);
        }
    }
    @DeleteMapping("/delete/{topping}")
    ToppingsResponse deleteTopping(@PathVariable String topping){
        try {
            Long toppingId = Long.parseLong(topping);

            return toppingsService.deleteTopping(Optional.of(toppingId), Optional.empty());
        }catch (NumberFormatException e){
            return toppingsService.deleteTopping(Optional.empty(), Optional.of(topping));
        }
    }
    @GetMapping("/getAll")
    ToppingsResponse getAllToppings(){
        return toppingsService.getAllToppings();
    }
}
