package com.PosSystem.POS.System.Service;

import com.PosSystem.POS.System.Dto.ToppingsDTO;
import com.PosSystem.POS.System.Dto.ToppingsResponse;

import java.util.Optional;

public interface ToppingsService {
    ToppingsResponse createTopping(ToppingsDTO toppingsDTO);
    ToppingsResponse updateTopping(Optional<Long> toppingId, Optional<String> toppingName, ToppingsDTO toppingsDTO);
    ToppingsResponse getAllToppings();
    ToppingsResponse deleteTopping(Optional<Long> toppingId, Optional<String> toppingName);


}
