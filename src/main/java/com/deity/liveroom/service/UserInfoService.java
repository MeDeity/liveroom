package com.deity.liveroom.service;


import com.deity.liveroom.entity.UserInfo;

public interface UserInfoService {

    UserInfo findUserByUserName(String userName);
}
