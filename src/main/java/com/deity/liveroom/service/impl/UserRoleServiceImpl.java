package com.deity.liveroom.service.impl;

import com.deity.liveroom.dao.UserRoleMapper;
import com.deity.liveroom.entity.UserPermission;
import com.deity.liveroom.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserPermission> findRoleByUserName(String userName) {
        return userRoleMapper.findRoleByUserName(userName);
    }
}
