package com.rao.itemservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVo {
    private String id;
    private String name;
    private String description;
    private String fatherId;
    private LocalDateTime createTime;
}
