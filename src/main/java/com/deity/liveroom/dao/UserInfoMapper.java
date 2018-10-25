package com.deity.liveroom.dao;


import com.deity.liveroom.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {

    @Select("SELECT * FROM user_info where username = #{userName}")
    UserInfo findUserByName(String userName);
}
