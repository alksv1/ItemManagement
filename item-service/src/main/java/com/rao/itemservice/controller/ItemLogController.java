package com.rao.itemservice.controller;

import com.rao.common.util.Result;
import com.rao.itemservice.service.LogService;
import com.rao.itemservice.vo.ItemLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-service")
public class ItemLogController {

    private final LogService logService;

    @Autowired
    public ItemLogController(LogService logService) {
        this.logService = logService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/items/{itemId}/log")
    public Result<?> getItemLog(@PathVariable String itemId,
                                @RequestParam(defaultValue = "1", required = false) Integer page,
                                @RequestParam(defaultValue = "30", required = false) Integer size) {
        List<ItemLogVo> itemLogVo = logService.getLogsByItemId(itemId, page, size);
        return Result.OK(itemLogVo);
    }
}
