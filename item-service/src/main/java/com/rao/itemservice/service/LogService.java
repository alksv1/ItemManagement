package com.rao.itemservice.service;

import com.github.pagehelper.PageHelper;
import com.rao.itemservice.dto.ItemLogDto;
import com.rao.itemservice.mapper.ItemLogMapper;
import com.rao.itemservice.po.ItemLogPo;
import com.rao.itemservice.vo.ItemLogVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final ItemLogMapper itemLogMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public LogService(ItemLogMapper itemLogMapper, ModelMapper modelMapper) {
        this.itemLogMapper = itemLogMapper;
        this.modelMapper = modelMapper;
    }

    public void keepLog(ItemLogDto itemLogDto) {
        itemLogMapper.InsertItemLog(itemLogDto);
    }

    public List<ItemLogVo> getLogsByItemId(String itemId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ItemLogPo> itemLogPoList = itemLogMapper.getLogsByItemId(itemId);
        return itemLogPoList.stream()
                .map(itemLogPo -> modelMapper.map(itemLogPo, ItemLogVo.class))
                .toList();
    }
}
