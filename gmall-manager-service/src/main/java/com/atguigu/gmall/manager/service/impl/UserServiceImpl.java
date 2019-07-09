package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.user.UserAddress;
import com.atguigu.gmall.manager.mapper.user.UserAddressMapper;
import com.atguigu.gmall.manager.user.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserAddressMapper userAddressMapper;
    /**
     * 获取用户收货信息
     * @param id
     * @return
     */
    @Override
    public List<UserAddress> getUserAddressByUserId(int id) {
        return userAddressMapper.selectList(new QueryWrapper<UserAddress>()
                .eq("user_id",id));
    }
}
