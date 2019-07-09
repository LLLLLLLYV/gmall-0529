package com.atguigu.gmall.util;

import com.alibaba.fastjson.JSON;
import org.springframework.util.Base64Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CookieUtils {


    private CookieUtils() {

    }

    public static Map<String,Object> resolveTokenData(String token) {
        String[] split = token.split("\\.");
        if (split != null && split.length == 3) {
            String s = split[1];
            byte[] bytes = Base64Utils.decodeFromString(s);
            try {
                //这是用户的json串
                String data = new String(bytes, "utf-8");
                return JSON.parseObject(data, Map.class);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }


   /* *//**
     * 获取指定cookie名的cookie的value值
     * @param cookieName
     * @param request
     * @return
     *//*
    public static String getCookieValue(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if(cookieName==name){
                return cookie.getValue();
            }
        }

        return null;
    }*/
    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie uid = new Cookie(name, null);
        uid.setPath("/");
        uid.setMaxAge(0);
        response.addCookie(uid);
    }

    /**
     * 获取cookie值
     *
     * @param request
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName) {
        Cookie cookies[] = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }

        }
        return null;
    }
}
