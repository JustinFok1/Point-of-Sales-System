package com.PosSystem.POS.System.Dto;

import com.PosSystem.POS.System.Entity.Categories;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResponse {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
