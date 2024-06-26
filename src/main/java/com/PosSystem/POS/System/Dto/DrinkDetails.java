package com.PosSystem.POS.System.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkDetails {
    private String drinkName;
    private BigDecimal price;
    private String categoryName;
}
