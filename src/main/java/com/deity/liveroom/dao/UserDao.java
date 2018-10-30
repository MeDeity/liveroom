package com.deity.liveroom.dao;


import com.deity.liveroom.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/6/12.
 */

@Repository
public interface UserDao extends CrudRepository<UserEntity,String> {
}
