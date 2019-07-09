package com.atguigu.gmall.manager.user;

import com.atguigu.gamll.user.UserAddress;

import java.util.List;

public interface UserService {
    /**
     * 查询用户的收货信息
     * @param id
     * @return
     */
    List<UserAddress> getUserAddressByUserId(int id);
}
