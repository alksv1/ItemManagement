package com.rao.itemservice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemLogPo {
    private Integer id;
    private String userId;
    private String targetId;
    private String methodName;
    private Integer num;
    private String args;
    private LocalDateTime operationTime;
}
