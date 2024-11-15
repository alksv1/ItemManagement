package com.rao.userservice.mapper;

import com.rao.userservice.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    UserPo getUserByEmail(String email);

    void insertUser(String id, String email);

    void updateUserInfo(UserPo userPo);

    UserPo getUserById(String userId);

    List<UserPo> getUserList();
}
