package com.PosSystem.POS.System.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class StripeResponse {
    private String responseCode;
    private String responseMessage;
    private String stripeCustomerId;
}
