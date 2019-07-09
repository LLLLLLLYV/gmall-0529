package com.atguigu.gmall.manager.user;

import com.atguigu.gamll.user.UserInfo;

public interface UserInfoSerivce {

    /**
     *
     * @param userInfo 根据账号和密码，查询详情
     * @return 返回用户在数据库的详细信息
     */
    UserInfo login (UserInfo userInfo);
}
