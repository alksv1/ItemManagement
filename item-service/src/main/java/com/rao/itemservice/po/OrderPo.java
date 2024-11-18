package com.rao.itemservice.po;

import com.rao.itemservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPo {
    String id;
    String userId;
    String itemId;
    Integer quantity;
    OrderStatus status;
    LocalDateTime operationTime;
}
