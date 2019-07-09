package com.atguigu.gmall.manager.cart;

import com.atguigu.gamll.cart.CartItem;
import com.atguigu.gamll.cart.CartVo;

import java.util.List;

public interface CartService {

    /**
     * 未登录时添加购物车
     * @param skuId
     * @param cartKey
     * @param num
     * @return
     */
    String addToCartUnLogin(Integer skuId, String cartKey, Integer num);

    /**
     * 已登录时添加购物车
     * @param skuId
     * @param userId
     * @param num
     */
    String addToCartLogin(Integer skuId, Integer userId, Integer num);

    /**
     * 合并购物车
     * @param cartKey
     * @param userId
     */
    void mergeCart(String cartKey, String userId);


    /**
     * 查看购物车数据
     * @param cartKey
     * @param login
     * @return
     */
    CartVo getAllCart(String cartKey, boolean login);

    /**
     * 返回购物车选中的商品
     * @param id
     * @return
     */
    List<CartItem> getCartInfoCheckdList(Integer id);

    void checkItem(Integer skuId, String termCartKey, int userId, boolean loginFlag, boolean isChecked);
}
