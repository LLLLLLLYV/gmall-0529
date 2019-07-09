package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gamll.order.TradePageVo;
import com.atguigu.gamll.user.UserAddress;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.cart.CartService;
import com.atguigu.gmall.manager.order.OrderService;
import com.atguigu.gmall.manager.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 交易结算
 */
@Controller
public class TradeController {

    @Reference
    CartService cartService;
    @Reference
    UserService userService;
    @Reference
    OrderService orderService;
    /**
     * 跳到结算页
     * @return
     */
    @LoginRequired
    @RequestMapping("/trade")
    public String trade(HttpServletRequest request) {
        //1.判断用户是否选中商品，如果没有选中，则回到购物车页
        Map<String,Object> userinfo = (Map<String, Object>) request.getAttribute("userinfo");
        int id = Integer.parseInt(userinfo.get("id").toString());
        //3.列举购物车选中的清单
        List<CartItem> cartItemList=cartService.getCartInfoCheckdList(id);
        //2.展示收货人信息
        List<UserAddress> userAddresses = userService.getUserAddressByUserId(id);


        //4.防止重复提交

        TradePageVo vo = new TradePageVo();
        vo.setCartItems(cartItemList);
        vo.setUserAddresses(userAddresses);



        //生成一个防重复提交令牌,并将令牌保存到redis
        String token = orderService.creatTradeToken();
        request.setAttribute("token",token);
        request.setAttribute("tradeInfo",vo);
        return "trade";
    }
}
