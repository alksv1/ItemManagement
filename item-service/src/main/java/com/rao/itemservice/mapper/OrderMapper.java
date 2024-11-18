package com.rao.itemservice.mapper;

import com.rao.itemservice.po.OrderPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    Integer getLoansNumber(String itemId);

    void createOrder(OrderPo orderPo);

    OrderPo getNormalOrderByUserIdAndItemId(String userId, String itemId);

    void updateOrder(OrderPo orderPo);

    void deleteOrder(String userId, String itemId);

    List<OrderPo> getAllOrderByUserId(String userId);
}
