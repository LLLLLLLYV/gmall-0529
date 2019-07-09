package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gmall.manager.cart.CartService;
import com.atguigu.gmall.manager.order.OrderService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    JedisPool jedisPool;
    @Reference
    CartService cartService;
    /**
     * 设置防止重复提交的token
     * @return
     */
    @Override
    public String creatTradeToken() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Jedis jedis = jedisPool.getResource();
        jedis.setex(token,60*3,"token");
        jedis.close();
        return token;
    }

    /**
     * 验证防止重复提交订单的token
     * @param token
     * @return
     */
    @Override
    public boolean verfyToken(String token) {
        Jedis jedis = jedisPool.getResource();
        Long del = jedis.del(token);
        return del==1L?true:false;
    }

    /**
     * 验证库存
     * @param userId
     * @return
     */
    @Override
    public List<String> verfyStock(Integer userId) throws IOException {

        List<CartItem> checkdList = cartService.getCartInfoCheckdList(userId);
        List<String> result = new ArrayList<>();
        for (CartItem cartItem : checkdList) {
            boolean b = stockCheck(cartItem.getSkuItem().getId(), cartItem.getNum());
            if(!b) {
                //没库存
                result.add(cartItem.getSkuItem().getSkuName());
            }
        }
        return result;
    }

    public boolean stockCheck(Integer Skuid,Integer num) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.gware.com/hasStock?skuId="+Skuid+"&num="+num);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            //获取相应体
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            return "0".equals(s)?false:true;
        }finally {
            response.close();
        }


    }
}
