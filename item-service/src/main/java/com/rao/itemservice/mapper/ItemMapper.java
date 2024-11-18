package com.rao.itemservice.mapper;


import com.rao.itemservice.dto.ItemRegisterDto;
import com.rao.itemservice.po.ItemPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    Integer updateItemInfoById(ItemPo itemPo);

    ItemPo selectItemById(String itemId);

    void insertItem(ItemRegisterDto itemRegisterDto);

    List<ItemPo> selectItemsList();

    List<ItemPo> selectItemAncestors(String itemId);

    int deleteItemById(String itemId);
}
