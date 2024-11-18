package com.rao.itemservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRegisterDto {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotNull
    private Integer availableQuantity;
    private String description;
    private String fatherId;
}
