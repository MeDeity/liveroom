package com.deity.liveroom.dao;


import com.deity.liveroom.entity.UserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM user_info " +
            "LEFT JOIN sys_user_role ON user_info.uid = sys_user_role.uid " +
            "LEFT JOIN sys_role ON sys_user_role.role_id = sys_role.id " +
            "LEFT JOIN sys_role_permission ON sys_role_permission.role_id = sys_role.id " +
            "LEFT JOIN sys_permission ON sys_permission.id = sys_role_permission.permission_id " +
            "WHERE user_info.username = #{userName}")
    List<UserPermission> findRoleByUserName(String userName);
}
