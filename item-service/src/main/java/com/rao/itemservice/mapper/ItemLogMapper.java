package com.rao.itemservice.mapper;

import com.rao.itemservice.dto.ItemLogDto;
import com.rao.itemservice.po.ItemLogPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemLogMapper {

//    @Insert("insert into item_log(user_id, target_id, method_name, num, args) VALUES " +
//            "(#{userId},#{targetId},#{methodName},#{num},#{args})")
//    void InsertItemLog(String userId, String targetId, String methodName, Integer num, String args);

    void InsertItemLog(ItemLogDto itemLogDto);

    List<ItemLogPo> getLogsByItemId(String itemId);
}
