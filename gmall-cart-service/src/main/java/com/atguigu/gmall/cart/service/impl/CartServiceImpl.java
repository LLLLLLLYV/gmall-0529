package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gamll.cart.CartVo;
import com.atguigu.gamll.cart.SkuItem;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gmall.cart.constant.CartConstant;
import com.atguigu.gmall.manager.cart.CartService;
import com.atguigu.gmall.manager.manager.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    JedisPool jedisPool;
    @Reference
    SkuService skuService;
    /**
     * 未登录
     * @param skuId
     * @param cartKey
     * @param num
     * @return
     */
    @Override
    public String addToCartUnLogin(Integer skuId, String cartKey, Integer num) {
        Jedis jedis = jedisPool.getResource();
        if(!StringUtils.isEmpty(cartKey)) {
            //之前创建过临时购物车
            //判断redis中是否存在这个购物车
            if(jedis.exists(cartKey) == false) {
                //如果不存在这个购物车(传来的购物车是非法的)，则新建购物车
                jedis.close();
                return createCart(skuId, num,false,null);
            }else {
                //判断购物车中是否有此购物项
                String cartItemJson = jedis.hget(cartKey, skuId + "");
                if(!StringUtils.isEmpty(cartItemJson)){
                    //有此商品，则叠加数量
                    //查询商品
                    CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
                    //设置数量
                    cartItem.setNum(cartItem.getNum()+num);
                    //设置价格
                    cartItem.setTotalPrice(cartItem.getNum()+num);
                    //将新的购物项转化成json，放到redis中
                    String newCartItemJson = JSON.toJSONString(cartItem);
                    jedis.hset(cartKey,skuId+"",newCartItemJson);
                }else {
                    //无此商品，则新增商品
                    SkuInfo skuInfo = null;
                    addCartItem(skuId,cartKey,num);

                }
            }


        } else {
            //没创建过临时购物车,新建购物车
            jedis.close();
            return createCart(skuId,num,false,null);
        }
        jedis.close();
        return cartKey;
    }

    /**
     * 已登录
     * @param skuId
     * @param userId
     * @param num
     */
    @Override
    public String addToCartLogin(Integer skuId, Integer userId, Integer num) {
        Jedis jedis = jedisPool.getResource();
        String cartKey = CartConstant.USER_CART_PREFIX + userId;
        Boolean exists = jedis.exists(cartKey);
        if(exists) {//该用户有购物车
            String cartItemJson = jedis.hget(cartKey,skuId+"");
            if(StringUtils.isEmpty(cartItemJson)) {//如果购物车没有该商品，否则增加购物项
                addCartItem(skuId,cartKey,num);
            }else {//如果购物车有该商品，则增加数量
                CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
                cartItem.setNum(cartItem.getNum()+num);
                cartItem.setTotalPrice(cartItem.getNum()+num);
                String newJson = JSON.toJSONString(cartItem);
                jedis.hset(cartKey,skuId+"",newJson);
            }
        }else {//如果该用户没有购物车，则创建购物车
            createCart(skuId,num,true,userId);
        }
        return cartKey;
    }

    /**
     * 合并购物车
     * @param cartKey
     * @param userId
     */
    @Override
    public void mergeCart(String cartKey, String userId) {
        //查出临时购物车的所有数据
        CartVo cartItemVo = getAllCart(cartKey, false);
        //如果临时购物车不为空
        List<CartItem> cartItems = cartItemVo.getCartItem();
        if(cartItems!=null){
            for (CartItem cartItem : cartItems) {
                //添加到用户购物车中
                addToCartLogin(cartItem.getSkuItem().getId(),Integer.valueOf(userId),cartItem.getNum());
            }

        }
    }

    /**
     * 查看购物车数据
     * @param cartKey 用户Id
     * @param login
     * @return
     */
    @Override
    public CartVo getAllCart(String cartKey, boolean login) {
        if(login) {//用户登录了
            cartKey = CartConstant.USER_CART_PREFIX+cartKey;
        }else {//用户没登录

        }
        //在redis中查询购物项数据，并添加到list集合中
        Jedis jedis = jedisPool.getResource();
        List<CartItem> cartItems = new ArrayList<>();
        Map<String, String> allCartItem = jedis.hgetAll(cartKey);
        for (Map.Entry<String, String> entry : allCartItem.entrySet()) {
            String value = entry.getValue();
            CartItem cartItem = JSON.parseObject(value, CartItem.class);
            cartItems.add(cartItem);
        }
        jedis.close();
        CartVo cartVo = new CartVo();
        cartVo.setCartItem(cartItems);

        return cartVo;
    }

    /**
     * 返回购物车选中的商品
     * @param id 用户id
     * @return  选中的商品的集合
     */
    @Override
    public List<CartItem> getCartInfoCheckdList(Integer id) {
        Jedis jedis = jedisPool.getResource();
        List<CartItem> cartItems = new ArrayList<>();
        //获取购物车所有商品
        CartVo allCart = getAllCart(id.toString(), true);
        if(allCart==null) {
            return null;
        }
        for (CartItem cartItem : allCart.getCartItem()) {
            if (cartItem.isCheck()) {
                cartItems.add(cartItem);
            }
        }
        if(cartItems.size()==0) {
            return null;
        }

        jedis.close();
        return cartItems;
    }

    @Override
    public void checkItem(Integer skuId, String termCartKey, int userId, boolean loginFlag, boolean isChecked) {
        String cartKey = loginFlag?CartConstant.USER_CART_PREFIX+userId:termCartKey;
        CartItem cartItemInfo = getCartItemInfo(cartKey, skuId);
        cartItemInfo.setCheck(isChecked);

        String s = JSON.toJSONString(cartItemInfo);
        Jedis jedis = jedisPool.getResource();
        jedis.hset(cartKey,skuId+"",s);
        jedis.close();
    }

    public CartItem getCartItemInfo(String cartKey, Integer skuId) {
        Jedis jedis = jedisPool.getResource();
        String json = jedis.hget(cartKey, skuId + "");
        CartItem cartItem = JSON.parseObject(json, CartItem.class);
        jedis.close();

        return cartItem;
    }

    /**
     * 添加购物项
     * @param skuId
     * @param num
     */
    public void addCartItem(Integer skuId, String cartKey, Integer num) {
        Jedis jedis = jedisPool.getResource();
        //查询商品skuInfo的信息
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuService.getSkuInfoBySkuId(skuId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //创建购物车项
        CartItem cartItem = new CartItem();
        //将skuInfo信息，添加到购物车项中
        SkuItem skuItem = new SkuItem();
        BeanUtils.copyProperties(skuInfo,skuItem);
        //封装购物项属性
        cartItem.setSkuItem(skuItem);
        cartItem.setNum(num);
        cartItem.setTotalPrice(num);
        //将购物项保存到redis中
        String jsonCartItem = JSON.toJSONString(cartItem);
        jedis.hset(cartKey,skuId+"",jsonCartItem);



        jedis.close();
    }


    /**
     * 创建购物车
     * @param skuId
     * @param num
     * @return
     */
    public String createCart(Integer skuId, Integer num,boolean login,Integer userId) {
        String newCartKey = null;
        if(login) {//如果用户已经登录
            newCartKey = CartConstant.USER_CART_PREFIX + userId;

        } else {//用户未登录
            newCartKey = CartConstant.TEAM_CART_PREFIX +
                    UUID.randomUUID().toString().substring(0, 10).replaceAll("-", "");
        }
        //没创建过临时购物车,新建购物车

        addCartItem(skuId,newCartKey,num);
        return newCartKey;
    }
}
