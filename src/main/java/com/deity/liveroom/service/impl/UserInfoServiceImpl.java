package com.deity.liveroom.service.impl;

import com.deity.liveroom.dao.UserInfoMapper;
import com.deity.liveroom.entity.UserInfo;
import com.deity.liveroom.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;


    @Override
    public UserInfo findUserByUserName(String userName) {
        return userInfoMapper.findUserByName(userName);
    }
}
