package com.PosSystem.POS.System.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private Integer orderNumber;
    private BigDecimal totalPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
    private OrderItemInfoDetails orderItemInfoDetails;
}
