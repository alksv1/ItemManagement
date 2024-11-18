package com.rao.itemservice.controller;

import com.rao.common.util.JwtUtil;
import com.rao.common.util.RequestHeaderUtil;
import com.rao.common.util.Result;
import com.rao.itemservice.service.BorrowService;
import com.rao.itemservice.service.OrderService;
import com.rao.itemservice.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-service")
public class BorrowController {
    //借（同物品数量增加需特判）、还、遗失、查看物品接環情況

    private final BorrowService borrowService;

    private final JwtUtil jwtUtil;
    private final OrderService orderService;

    @Autowired
    public BorrowController(BorrowService borrowService,
                            JwtUtil jwtUtil, OrderService orderService) {
        this.borrowService = borrowService;
        this.jwtUtil = jwtUtil;
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/api/items/{itemId}/loan")//正数借负数还
    public Result<?> loanItem(@PathVariable String itemId, @RequestParam Integer num) {
        borrowService.createOrUpdateOrder(jwtUtil.getUserIdFromToken(RequestHeaderUtil.getToken()), itemId, num);
        return Result.OK(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/api/items/{itemId}/disrupt")
    public Result<?> disruptItem(@PathVariable String itemId) {
        borrowService.setDisrupt(
                jwtUtil.getUserIdFromToken(
                        RequestHeaderUtil.getToken()),
                itemId);
        return Result.OK(null);
    }


    //获取当前用户所有订单，用户订单量应该不多，所以直接查询所有
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/orders/me")
    public Result<?> getCurrentUserOrders(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "30") Integer size) {
        List<OrderVo> orderVoList = orderService.getOrderInfoByUserId(
                jwtUtil.getUserIdFromToken(
                        RequestHeaderUtil.getToken()
                ), page, size);
        return Result.OK(orderVoList);
    }

    //按ID获取用户所有订单
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/orders")
    public Result<?> getUserOrdersByUserId(@RequestParam String userId,
                                           @RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "30") Integer size) {
        List<OrderVo> orderVoList = orderService.getOrderInfoByUserId(userId, page, size);
        return Result.OK(orderVoList);
    }
}