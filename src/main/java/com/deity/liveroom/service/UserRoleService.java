package com.deity.liveroom.service;


import com.deity.liveroom.entity.UserPermission;

import java.util.List;

public interface UserRoleService {

    List<UserPermission> findRoleByUserName(String userName);
}
