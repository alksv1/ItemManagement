package com.rao.itemservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemLogVo {
    private String userId;
    private String targetId;
    private String methodName;
    private Integer num;
    private String args;
}
