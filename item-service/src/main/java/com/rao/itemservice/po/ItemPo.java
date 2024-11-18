package com.rao.itemservice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPo {
    private String id;
    private String name;
    private String description;
    private String fatherId;
    private Integer availableQuantity;
    private String coverImage;
    private LocalDateTime createTime;
}
