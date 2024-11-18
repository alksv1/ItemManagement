package com.rao.itemservice.service;

import com.github.pagehelper.PageHelper;
import com.rao.itemservice.mapper.OrderMapper;
import com.rao.itemservice.po.OrderPo;
import com.rao.itemservice.vo.OrderVo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final ModelMapper modelMapper;

    public OrderService(OrderMapper orderMapper, ModelMapper modelMapper) {
        this.orderMapper = orderMapper;
        this.modelMapper = modelMapper;
    }

    public List<OrderVo> getOrderInfoByUserId(String userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrderPo> orderPoList = orderMapper.getAllOrderByUserId(userId);
        return orderPoList.stream()
                .map(orderPo -> modelMapper.map(orderPo, OrderVo.class))
                .toList();
    }
}
