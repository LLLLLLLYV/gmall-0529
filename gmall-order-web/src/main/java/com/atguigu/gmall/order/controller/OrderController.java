package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.order.OrderSubmitVo;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    @Reference
    OrderService orderService;

    @LoginRequired
    @RequestMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo submitVo , HttpServletRequest request) {
        System.out.println(submitVo.getOrderComment()+"+++"+submitVo.getUserAddressId()+"+++"+submitVo.getToken());

        //防止重复提交
        boolean token = orderService.verfyToken(submitVo.getToken());
        if(!token) {
            //令牌失效
            request.setAttribute("errorMsg","订单提交失败，请刷新购物车后重新下单");
            return "tradeFail";
        }
        Map<String,Object> userinfo = (Map<String, Object>) request.getAttribute("userinfo");
        //验证库存
        List<String> stock = orderService.verfyStock(Integer.parseInt(userinfo.get("id")+""));
        return "list";
    }
}
