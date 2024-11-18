package com.rao.itemservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateDto {
    private String name;
    private String description;
    private String fatherId;
}
