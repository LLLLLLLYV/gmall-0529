package com.atguigu.gamll.cart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车的每项数据
 *
 */
@Getter
public class CartItem implements Serializable {

    @Setter
    private SkuItem skuItem;//商品的基本信息
    @Setter
    private Integer num;//当前项数量
    private BigDecimal totalPrice;//当前项的总价
    @Setter
    private boolean isCheck = false;

    public void setTotalPrice(Integer num) {
        totalPrice = skuItem.getPrice().multiply(new BigDecimal(num));
    }
}
