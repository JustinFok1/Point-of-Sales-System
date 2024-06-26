package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dao.ToppingsRepo;
import com.PosSystem.POS.System.Dto.ToppingsDTO;
import com.PosSystem.POS.System.Dto.ToppingsResponse;
import com.PosSystem.POS.System.Entity.Toppings;
import com.PosSystem.POS.System.Utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToppingsServiceImpl implements ToppingsService {
    @Autowired
    ToppingsRepo toppingsRepo;

    @Override
    public ToppingsResponse createTopping(ToppingsDTO toppingsDTO) {
        if (toppingExist(toppingsDTO.getToppingName())) {
            return ToppingsResponse.builder()
                    .responseCode(Messages.ALREADY_EXISTS_CODE)
                    .responseMessage(Messages.ALREADY_EXISTS_MESSAGE)
                    .build();
        }

        Toppings newTopping = Toppings.builder()
                .toppingName(toppingsDTO.getToppingName())
                .price(toppingsDTO.getPrice())
                .build();

        toppingsRepo.save(newTopping);

        return ToppingsResponse.builder()
                .responseCode(Messages.CREATED_CODE)
                .responseMessage(Messages.CREATED_MESSAGE)
                .toppingName(newTopping.getToppingName())
                .price(newTopping.getPrice())
                .build();
    }


    @Override
    public ToppingsResponse updateTopping(Optional<Long> toppingId, Optional<String> toppingName, ToppingsDTO toppingsDTO) {
        Optional<Toppings> toppingToUpdate = findTopping(toppingId, toppingName);

        if(toppingToUpdate.isEmpty()){
            return ToppingsResponse.builder()
                    .responseCode(Messages.TOPPING_DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.TOPPING_DOES_NOT_EXIST_MESSAGE)
                    .build();
        }
        if(toppingExist(toppingsDTO.getToppingName())){
            return ToppingsResponse.builder()
                    .responseCode(Messages.ALREADY_EXISTS_CODE)
                    .responseMessage(Messages.ALREADY_EXISTS_MESSAGE)
                    .build();
        }

        toppingToUpdate.get().setToppingName(toppingsDTO.getToppingName());
        toppingToUpdate.get().setPrice(toppingsDTO.getPrice());

        toppingsRepo.save(toppingToUpdate.get());

        return ToppingsResponse.builder()
                .responseCode(Messages.UPDATED_CODE)
                .responseMessage(Messages.UPDATED_MESSAGE)
                .toppingName(toppingToUpdate.get().getToppingName())
                .price(toppingToUpdate.get().getPrice())
                .build();
    }


    @Override
    public ToppingsResponse getAllToppings() {
        List<Toppings> toppingsList = toppingsRepo.findAll();

        List<ToppingsDTO> toppingDTOS = toppingsList.stream()
                .map(topping -> ToppingsDTO.builder()
                        .toppingName(topping.getToppingName())
                        .price(topping.getPrice())
                        .build())
                .collect(Collectors.toList());

        if (toppingsList.isEmpty()) {
            return ToppingsResponse.builder()
                    .responseCode(Messages.EMPTY_CODE)
                    .responseMessage(Messages.EMPTY_MESSAGE)
                    .toppings(null)
                    .build();
        }
        return ToppingsResponse.builder()
                .responseCode(Messages.SUCCESS_CODE)
                .responseMessage(Messages.SUCCESS_MESSAGE)
                .toppings(toppingDTOS)
                .build();
    }


    @Override
    public ToppingsResponse deleteTopping(Optional<Long> toppingId, Optional<String> toppingName) {
        Optional<Toppings> toppingToDelete = findTopping(toppingId, toppingName);

        if(toppingToDelete.isEmpty()){
            return ToppingsResponse.builder()
                    .responseCode(Messages.DOES_NOT_EXIST_CODE)
                    .responseMessage(Messages.DOES_NOT_EXIST_MESSAGE)
                    .build();
        }

        toppingsRepo.delete(toppingToDelete.get());

        return ToppingsResponse.builder()
                .responseCode(Messages.DELETED_CODE)
                .responseMessage(Messages.DELETED_MESSAGE)
                .build();
    }

    Optional<Toppings> findTopping (Optional<Long> toppingId, Optional<String> toppingName){
        Optional<Toppings> findTopping = Optional.empty();

        if(toppingId.isPresent() && toppingsRepo.existsById(toppingId.get())){
            findTopping = toppingsRepo.findById(toppingId.get());
        } else if (toppingName.isPresent() && toppingsRepo.existsByToppingName(toppingName.get())){
            findTopping = toppingsRepo.findByToppingName(toppingName.get());
        }
        return findTopping;
    }
    public boolean toppingExist(String toppingName){
        return toppingsRepo.existsByToppingName(toppingName);
    }
}
