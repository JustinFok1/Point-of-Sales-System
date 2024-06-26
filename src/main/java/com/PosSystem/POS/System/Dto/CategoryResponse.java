package com.PosSystem.POS.System.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String responseCode;
    private String responseMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String categoryName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryDetails> categoryDetails;

}
