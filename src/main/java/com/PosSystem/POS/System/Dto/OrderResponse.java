package com.PosSystem.POS.System.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String responseCode;
    private String responseMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalPrice;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderDetails> orderDetails;
}
