package com.rao.itemservice.controller;


import com.rao.common.exception.ParameterErrorException;
import com.rao.common.util.Result;
import com.rao.itemservice.dto.ItemRegisterDto;
import com.rao.itemservice.dto.ItemUpdateDto;
import com.rao.itemservice.service.ItemService;
import com.rao.itemservice.service.MinIOService;
import com.rao.itemservice.vo.ItemVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/item-service")
public class ItemController {

    private final MinIOService minIOService;

    private final ItemService itemService;

    @Autowired
    public ItemController(MinIOService minIOService,
                          ItemService itemService) {
        this.minIOService = minIOService;
        this.itemService = itemService;
    }

    //上传并绑定物品图片
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/items/{itemId}/cover/upload")
    public Result<?> uploadItemCover(@RequestBody MultipartFile cover, @PathVariable String itemId) {
        String filename = minIOService.storePic(cover);
        itemService.updateItemCover(filename, itemId);
        return Result.OK(null);
    }

    //获取物品图片url
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/{itemId}/cover")
    public Result<?> getItemCover(@PathVariable String itemId) {
        String url = itemService.getCoverUrl(itemId);
        return Result.OK(url);
    }

    //获取物品信息
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/{itemId}/info")
    public Result<?> getItemInfo(@PathVariable String itemId) {
        ItemVo itemVo = itemService.getItemInfo(itemId);
        return Result.OK(itemVo);
    }

    //注册物品
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/items/register")
    public Result<?> registerItem(@Valid @RequestBody ItemRegisterDto itemRegisterDto) {
        itemService.insertItem(itemRegisterDto);
        return Result.OK(null);
    }

    //修改物品信息，可修改字段：name. description, father_id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/items/{itemId}/info")
    public Result<?> updateItemInfo(@PathVariable String itemId, @RequestBody ItemUpdateDto itemUpdateDto) {
        itemService.updateItem(itemId, itemUpdateDto);
        return Result.OK(null);
    }

    //获取物品列表，扁平化数据需前端处理为树形数据
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/list")
    public Result<?> getItemsList() {//数据量应该不会过五千，不分页了
        List<ItemVo> itemVoList = itemService.getItemsList();
        return Result.OK(itemVoList);
    }

    //获取物品所有祖先
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/{itemId}/ancestors")
    public Result<?> getItemAncestors(@PathVariable String itemId) {
        List<ItemVo> ancestors = itemService.getAncestors(itemId);
        return Result.OK(ancestors);
    }

    //删除物品
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/items/{itemId}")
    public Result<?> deleteItemById(@PathVariable String itemId) {
        itemService.deleteItemById(itemId);
        return Result.OK(null);
    }

    //按增量修改物品数量，不用定量的原因是日志不好记录
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/items/{itemId}/quantity")
    public Result<?> addItemQuantity(@PathVariable String itemId, @RequestParam Integer numberOfChange) {
        if (numberOfChange == 0) throw new ParameterErrorException();
        itemService.addItemQuantity(itemId, numberOfChange);
        return Result.OK(null);
    }

    //获取物品总数量，不算遗失/损坏的
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/{itemId}/quantity")
    public Result<?> getItemQuantity(@PathVariable String itemId) {
        Integer quantity = itemService.getItemQuantity(itemId);
        return Result.OK(quantity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/items/search")
    public Result<?> searchItem(@RequestParam String name) {
        List<ItemVo> itemVoList = itemService.searchItem(name);
        return Result.OK(itemVoList);
    }
}