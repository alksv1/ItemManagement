package com.rao.itemservice.vo;

import com.rao.itemservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    private String userId;
    private String itemId;
    private Integer quantity;
    private OrderStatus status;
    private LocalDateTime operationTime;
}
