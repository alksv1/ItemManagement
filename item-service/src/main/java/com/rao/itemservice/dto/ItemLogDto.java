package com.rao.itemservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemLogDto {
    private String userId;
    private String targetId;
    private String methodName;
    private Integer num;
    private String args;
}
