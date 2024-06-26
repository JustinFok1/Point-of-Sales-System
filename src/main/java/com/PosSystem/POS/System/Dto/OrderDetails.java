package com.PosSystem.POS.System.Dto;

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
    private OrderItemInfoDetails orderItemInfoDetails;
}
