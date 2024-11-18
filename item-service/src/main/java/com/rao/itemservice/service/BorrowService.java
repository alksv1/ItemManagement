package com.rao.itemservice.service;

import com.rao.common.exception.ParameterErrorException;
import com.rao.common.exception.ResourceNotFoundException;
import com.rao.itemservice.dto.ItemLogDto;
import com.rao.itemservice.enums.OrderStatus;
import com.rao.itemservice.mapper.ItemMapper;
import com.rao.itemservice.mapper.OrderMapper;
import com.rao.itemservice.po.ItemPo;
import com.rao.itemservice.po.OrderPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BorrowService {

    private final OrderMapper orderMapper;

    private final LogService logService;

    private final ItemMapper itemMapper;

    @Autowired
    public BorrowService(OrderMapper orderMapper,
                         LogService logService, ItemMapper itemMapper) {
        this.orderMapper = orderMapper;
        this.logService = logService;
        this.itemMapper = itemMapper;
    }

    @Transactional
    public void createOrUpdateOrder(String userIdFromToken, String itemId, Integer num) {
        ItemPo itemPo = itemMapper.selectItemById(itemId);
        if (itemPo == null) throw new ResourceNotFoundException();
        //限制不能超借
        if (itemPo.getAvailableQuantity() - num < 0 || num > itemPo.getAvailableQuantity())
            throw new ParameterErrorException();
        OrderPo preOrder = orderMapper.getNormalOrderByUserIdAndItemId(userIdFromToken, itemId);

        OrderPo orderPo = new OrderPo();
        if (preOrder == null) {
            if (num < 0) throw new ParameterErrorException();
            orderPo.setUserId(userIdFromToken);
            orderPo.setItemId(itemId);
            orderPo.setQuantity(num);
            orderMapper.createOrder(orderPo);
            logService.keepLog(new ItemLogDto(
                    userIdFromToken, itemId, "createOrUpdateOrder", num, null));
        } else {
            //限制不能超还
            if (preOrder.getQuantity() + num < 0)
                throw new ParameterErrorException();
            orderPo.setId(preOrder.getId());
            orderPo.setQuantity(preOrder.getQuantity() + num);
            if (orderPo.getQuantity() == 0)
                orderMapper.deleteOrder(userIdFromToken, itemId);
            else
                orderMapper.updateOrder(orderPo);
        }

        ItemPo afterItem = new ItemPo();
        afterItem.setId(itemId);
        afterItem.setAvailableQuantity(itemPo.getAvailableQuantity() - num);
        itemMapper.updateItemInfoById(afterItem);

        logService.keepLog(new ItemLogDto(
                userIdFromToken,
                itemId,
                "createOrUpdateOrder",
                num,
                null
        ));
    }

    @Transactional
    public void setDisrupt(String userIdFromToken, String itemId) {
        OrderPo orderPo = orderMapper.getNormalOrderByUserIdAndItemId(userIdFromToken, itemId);
        if (orderPo == null) throw new ResourceNotFoundException();
        OrderPo order = new OrderPo();
        order.setId(orderPo.getId());
        order.setStatus(OrderStatus.DISRUPT);
        orderMapper.updateOrder(order);

        logService.keepLog(new ItemLogDto(
                userIdFromToken,
                itemId,
                "setDisrupt",
                orderPo.getQuantity(),
                null
        ));
    }
}
