package com.PosSystem.POS.System.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemInfoDetails {
    private String categoryName;
    private String drinkName;
    private BigDecimal drinkPrice;
    private List<ToppingDetails> toppings;
}
