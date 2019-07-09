package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gamll.user.UserInfo;
import com.atguigu.gmall.manager.user.UserInfoSerivce;
import com.atguigu.gmall.passport.utils.JwtUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Reference
    UserInfoSerivce userInfoSerivce;
    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(UserInfo userInfo, String originUrl
                        , @CookieValue(name = CookieConstant.SSO_COOKIE_NAME,required = false) String token
                        , HttpServletResponse response,HttpServletRequest request) {


        //1.登录过
        if(!StringUtils.isEmpty(token)) {
            //登录过了，重定向到之前的页面

            return "redirect:"+originUrl+"?token="+token;
        } else {
            //2.没登录过
            if(userInfo.getLoginName()==null) {
                //去登陆页
                return "index";
            } else {
                //用户填写了用户信息
                UserInfo login = userInfoSerivce.login(userInfo);
                if(login != null) {
                    //用户登录成功，回到之前的页面
                    Map<String,Object> body =new HashMap<>();
                    body.put("id",login.getId());
                    body.put("loginName",login.getLoginName());
                    body.put("nickName",login.getNickName());
                    body.put("headImg",login.getHeadImg());
                    body.put("email",login.getEmail());

                    String newToken = JwtUtils.createJwtToken(body);
                    //本sso域也要在cookie中保存令牌
                    Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, newToken);
                    cookie.setPath("/");//无论网站那一层都能用
                    response.addCookie(cookie);

                    if(originUrl == null) {
                       return "redirect:http://www.gmall.com" ;
                    }

                    return "redirect:"+originUrl+"?token="+newToken;
                } else {
                    //登录失败，重新登录
                    return "index";
                }
            }
        }
    }
    @ResponseBody
    @RequestMapping("/confirmToken")
    public String confirmToken(String token) {
        boolean b = JwtUtils.confirmJwtToken(token);
        return b?"ok":"error";
    }
}
