package com.rao.itemservice.service;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.rao.common.exception.ParameterErrorException;
import com.rao.common.exception.ResourceAlreadyExistsException;
import com.rao.common.exception.ResourceNotFoundException;
import com.rao.common.util.JwtUtil;
import com.rao.common.util.RequestHeaderUtil;
import com.rao.itemservice.dto.ItemLogDto;
import com.rao.itemservice.dto.ItemRegisterDto;
import com.rao.itemservice.dto.ItemUpdateDto;
import com.rao.itemservice.mapper.ItemMapper;
import com.rao.itemservice.mapper.OrderMapper;
import com.rao.itemservice.po.ItemPo;
import com.rao.itemservice.vo.ItemVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ItemService {

    private final ItemMapper itemMapper;

    private final MinIOService minIOService;

    private final ModelMapper modelMapper;

    private final OrderMapper orderMapper;

    private final LogService logService;

    private final JwtUtil jwtUtil;

    @Autowired
    public ItemService(ItemMapper itemMapper,
                       MinIOService minIOService,
                       ModelMapper modelMapper,
                       OrderMapper orderMapper,
                       LogService logService, JwtUtil jwtUtil) {
        this.itemMapper = itemMapper;
        this.minIOService = minIOService;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
        this.logService = logService;
        this.jwtUtil = jwtUtil;
    }

    public void updateItemCover(String filename, String itemId) {
        ItemPo item = itemMapper.selectItemById(itemId);
        if (item == null) throw new ResourceNotFoundException();

        if (!item.getCoverImage().equals("default_cover.png"))
            minIOService.removeFile(item.getCoverImage());

        ItemPo itemPo = new ItemPo();
        itemPo.setId(itemId);
        itemPo.setCoverImage(filename);
        itemMapper.updateItemInfoById(itemPo);
    }

    public String getCoverUrl(String itemId) {
        ItemPo itemPo = itemMapper.selectItemById(itemId);
        if (itemPo == null) throw new ResourceNotFoundException();
        return minIOService.getPicURL(itemPo.getCoverImage());
    }

    public ItemVo getItemInfo(String itemId) {
        ItemPo itemPo = itemMapper.selectItemById(itemId);
        if (itemPo == null) throw new ResourceNotFoundException();
        return modelMapper.map(
                itemPo,
                ItemVo.class
        );
    }

    public void insertItem(ItemRegisterDto itemRegisterDto) {
        if (itemMapper.selectItemById(itemRegisterDto.getId()) != null)
            throw new ResourceAlreadyExistsException();
        itemMapper.insertItem(itemRegisterDto);
    }

    @Transactional
    public void updateItem(String itemId, ItemUpdateDto itemUpdateDto) {

        ItemPo map = modelMapper.map(itemUpdateDto, ItemPo.class);
        map.setId(itemId);

        if (itemMapper.updateItemInfoById(map) == 0) throw new ResourceNotFoundException();

        logService.keepLog(
                new ItemLogDto(
                        jwtUtil.getUserIdFromToken(RequestHeaderUtil.getToken()),
                        itemId,
                        "updateItem",
                        null,
                        JSON.toJSONString(itemUpdateDto)));
    }

    public List<ItemVo> getItemsList() {
        List<ItemPo> itemPoList = itemMapper.selectItemsList();
        return itemPoList.stream()
                .map(itemVo -> modelMapper.map(itemVo, ItemVo.class))
                .toList();
    }

    public List<ItemVo> getAncestors(String itemId) {
        List<ItemPo> itemPoList = itemMapper.selectItemAncestors(itemId);
        if (itemPoList.isEmpty()) throw new ResourceNotFoundException();
        return itemPoList.stream()
                .map(itemVo -> modelMapper.map(itemVo, ItemVo.class))
                .toList();
    }

    public void deleteItemById(String itemId) {
        if (itemMapper.deleteItemById(itemId) == 0) throw new ResourceNotFoundException();
    }

    @Transactional
    public void addItemQuantity(String itemId, Integer numberOfChange) {
        ItemPo itemPo = itemMapper.selectItemById(itemId);
        int afterQuantity = itemPo.getAvailableQuantity() + numberOfChange;
        if (afterQuantity < 0) throw new ParameterErrorException();
        ItemPo newItem = new ItemPo();
        newItem.setAvailableQuantity(afterQuantity);
        newItem.setId(itemId);
        itemMapper.updateItemInfoById(newItem);
        logService.keepLog(
                new ItemLogDto(
                        jwtUtil.getUserIdFromToken(RequestHeaderUtil.getToken()),
                        itemId,
                        "addItemQuantity",
                        numberOfChange,
                        null));
    }

    public Integer getItemQuantity(String itemId) {
        ItemPo itemPo = itemMapper.selectItemById(itemId);
        if (itemPo == null) throw new ResourceNotFoundException();
        Integer number = orderMapper.getLoansNumber(itemId);
        if (number == null) number = 0;
        return itemPo.getAvailableQuantity() + number;
    }

    public List<ItemVo> searchItem(String name) {
        List<ItemPo> itemPoList = itemMapper.searchItemByName(name);
        return itemPoList.stream()
                .map(itemPo -> modelMapper.map(itemPo, ItemVo.class))
                .toList();
    }
}
