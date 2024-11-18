package com.rao.userservice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {

    @Insert("insert into user_log(user_id,target_id,method_name,args) values (#{userId},#{targetId},#{methodName},#{json})")
    void insertUserLog(String userId, String targetId, String methodName, String json);
}
