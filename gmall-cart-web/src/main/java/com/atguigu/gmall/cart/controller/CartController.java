package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gamll.cart.CartVo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.manager.cart.CartService;
import com.atguigu.gmall.manager.manager.SkuService;
import com.atguigu.gmall.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class CartController {
    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;
    @LoginRequired(needLogin = false)
    @ResponseBody
    @RequestMapping("/checkCart")
    public String checkItem(boolean isChecked,Integer skuId,HttpServletRequest request) {
        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        String termCartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);

        boolean loginFlag = userInfo==null?false:true;
        int userId = 0;
        try{
            userId = Integer.parseInt(userInfo.get("id").toString());
        }catch (Exception e){}


        cartService.checkItem(skuId,termCartKey,userId,loginFlag,isChecked);
        return "ok";
    }



    /**
     * 查看购物车的数据
     * @return
     */
    @LoginRequired(needLogin = false)
    @RequestMapping("/cartList")
    public String cartInfoPage(HttpServletRequest request,HttpServletResponse response) {

        Map<String,Object> userInfo = (Map<String, Object>) request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        String tempCart = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);

        String cartKey = null;
        boolean login = false;
        if(userInfo!=null) { //登录了
            login = true;
            cartKey = userInfo.get("id").toString();
            //如果用户已经登录，并且有临时购物车，则合并购物车
            if(!StringUtils.isEmpty(tempCart)) {
                //合并购物车
                cartService.mergeCart(tempCart, userInfo.get("id").toString());
                //删除临时购物车
                CookieUtils.removeCookie(response,CookieConstant.COOKIE_CART_KEY);
            }
        } else {//没登录
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        }
        //查询购物车，并设置到域中
        CartVo cartVo = cartService.getAllCart(cartKey,login);

        request.setAttribute("cartVo",cartVo);
        return "cartList";
    }


    /**
     * 将制定商品添加到购物车
     * @param skuId 指定商品的skuId
     * @param num 指定商品的数量
     * @return
     */
    @LoginRequired(needLogin = false)
    @RequestMapping("/addToCart")
    public String addToCart(Integer skuId, Integer num,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String,Object> loginUser = (Map<String,Object>)request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        String cartId = null;
        if(loginUser == null) { //未登录时添加购物车
            String cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            if(StringUtils.isEmpty(cartKey)) { //判断购物车的cookie是否存在，如果没有则创建
                cartId = cartService.addToCartUnLogin(skuId,null,num);
                //创建临时购物车的cookie
                CookieUtils.addCookie(response,CookieConstant.COOKIE_CART_KEY,cartId,CookieConstant.COOKIE_CART_KEY_MAX_AGE);
            } else {
                cartService.addToCartUnLogin(skuId,cartKey,num);
            }

        }else {//登录时添加购物车
            //将制定商品添加到购物车，并合并未登录时的购物车
            String cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            Integer userId =Integer.parseInt(loginUser.get("id").toString());
            if(StringUtils.isEmpty(cartKey)) {
                //没有临时购物车
                cartService.addToCartLogin(skuId,userId,num);
            } else {
                //有临时购物车，先和并购物车
                cartService.mergeCart(cartKey,userId.toString());
                //再加入购物车
                cartService.addToCartLogin(skuId,userId,num);
                //删除临时购物车
                CookieUtils.removeCookie(response,CookieConstant.COOKIE_CART_KEY);
            }


        }
        try {
            SkuInfo skuInfo = skuService.getSkuInfoBySkuId(skuId);
            request.setAttribute("skuInfo",skuInfo);
            request.setAttribute("num",num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
