package com.atguigu.gmall.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.user.UserInfo;
import com.atguigu.gmall.manager.user.UserInfoSerivce;
import com.atguigu.gmall.passport.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserInfoServiceImpl implements UserInfoSerivce {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public UserInfo login(UserInfo userInfo) {
        //将传入的密码MD5加密，设置到传来的userInfo中
        userInfo.setPasswd(DigestUtils.md5Hex(userInfo.getPasswd()));
        //在数据库中查询

        return userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("login_name",userInfo.getLoginName())
                .eq("passwd",userInfo.getPasswd())
        );
    }
}
