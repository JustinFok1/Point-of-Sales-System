package com.PosSystem.POS.System.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinkResponse {
    private String responseCode;
    private String responseMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String drinkName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DrinkDetails> drinks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DrinkDetails drinkDetails;
}
